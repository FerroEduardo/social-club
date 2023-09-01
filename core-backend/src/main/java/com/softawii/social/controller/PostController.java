package com.softawii.social.controller;

import com.softawii.social.exception.FailedToCreateImageException;
import com.softawii.social.model.Game;
import com.softawii.social.model.Image;
import com.softawii.social.model.User;
import com.softawii.social.model.dto.request.comment.CommentDTO;
import com.softawii.social.model.dto.request.comment.CreatePostCommentRequestDTO;
import com.softawii.social.model.dto.request.post.CreatePostRequestDTO;
import com.softawii.social.model.dto.request.post.IndexPostCommentsRequestDTO;
import com.softawii.social.model.dto.request.post.IndexPostRequestDTO;
import com.softawii.social.model.dto.request.post.PostDTO;
import com.softawii.social.repository.PostRepository;
import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;

@RestController
@RequestMapping(value = "post")
@Validated
public class PostController {

    private final UserService     userService;
    private final GameService     gameService;
    private final PostService     postService;
    private final ImageService    imageService;
    private final PostVoteService postVoteService;
    private final CommentService  commentService;
    private final PostRepository  test;

    public PostController(UserService userService, GameService gameService, PostService postService, ImageService imageService, PostVoteService postVoteService, CommentService commentService, PostRepository test) {
        this.userService = userService;
        this.gameService = gameService;
        this.postService = postService;
        this.imageService = imageService;
        this.postVoteService = postVoteService;
        this.commentService = commentService;
        this.test = test;
    }

    @GetMapping
    public Iterable<?> index(@Valid IndexPostRequestDTO dto, OAuth2AuthenticationToken authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return this.postService.findAll(principal.getId(), dto.getPage().intValue(), dto.getSize().intValue());
    }

    @GetMapping("{postId}")
    public ResponseEntity<?> show(@PathVariable Long postId, OAuth2AuthenticationToken authentication) {
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            PostDTO       post      = this.postService.findById(postId, principal.getId()).orElseThrow();

            return ResponseEntity.ok(post);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{postId}/comment")
    public ResponseEntity<?> indexComments(@Valid IndexPostCommentsRequestDTO request, @PathVariable Long postId) {
        try {
            Page<CommentDTO> comments = this.commentService.findAll(postId, request.getPage(), request.getSize());

            return ResponseEntity.ok(comments);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("{postId}/comment")
    public ResponseEntity<?> indexComments(@PathVariable Long postId, @Valid @RequestBody CreatePostCommentRequestDTO request, OAuth2AuthenticationToken authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        try {
            Matcher matcher = CommentService.COMMENT_VALUE_PATTERN.matcher(request.getValue());
            String  value      = matcher.replaceAll("$1").trim();
            return ResponseEntity.ok(this.commentService.create(postId, principal.getId(), value));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            OAuth2AuthenticationToken authentication,
            @Valid @ModelAttribute CreatePostRequestDTO dto
    ) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User          user          = userService.findByEmail(userPrincipal.getEmail()).get();

        Optional<Game> gameOptional = gameService.findById(dto.getGameId());
        if (gameOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Game not found"));
        }
        Game  game = gameOptional.get();
        Image image;
        try {
            byte[] imageBytes = dto.getImage().getBytes();
            image = imageService.create(imageBytes);
        } catch (IOException | FailedToCreateImageException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Unable to save image"));
        }
        Number postId = postService.create(user, game, image, dto.getTitle(), dto.getDescription());

        return ResponseEntity.ok(Map.of("id", postId));
    }

    @PostMapping("{postId}/vote/{voteValue}")
    public ResponseEntity<?> vote(
            @PathVariable Long postId,
            @PathVariable Long voteValue,
            OAuth2AuthenticationToken authentication
    ) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            if (voteValue == 1 || voteValue == -1 || voteValue == 0) {
                long userId = userPrincipal.getId();
                this.postService.findById(postId, userId).orElseThrow();

                this.postVoteService.vote(postId, userId, voteValue);
                Long reputation = this.postVoteService.getPostReputation(postId);

                return ResponseEntity.ok(Map.of("reputation", reputation));
            }
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid 'value' field"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Post does not exists"));
        }
    }
}
