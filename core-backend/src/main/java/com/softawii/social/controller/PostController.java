package com.softawii.social.controller;

import com.softawii.social.exception.FailedToCreateImageException;
import com.softawii.social.model.Game;
import com.softawii.social.model.Post;
import com.softawii.social.model.User;
import com.softawii.social.model.dto.PostDTO;
import com.softawii.social.request.comment.CreatePostCommentRequest;
import com.softawii.social.request.post.CreatePostRequest;
import com.softawii.social.request.post.EditPostRequest;
import com.softawii.social.request.post.IndexPostCommentsRequest;
import com.softawii.social.request.post.IndexPostRequest;
import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping(value = "post")
@Validated
public class PostController {

    private final Logger          logger = LoggerFactory.getLogger(PostController.class);
    private final UserService     userService;
    private final GameService     gameService;
    private final PostService     postService;
    private final ImageService    imageService;
    private final PostVoteService postVoteService;
    private final CommentService  commentService;

    public PostController(
            UserService userService,
            GameService gameService,
            PostService postService,
            ImageService imageService,
            PostVoteService postVoteService,
            CommentService commentService
    ) {
        this.userService = userService;
        this.gameService = gameService;
        this.postService = postService;
        this.imageService = imageService;
        this.postVoteService = postVoteService;
        this.commentService = commentService;
    }

    @GetMapping
    public Iterable<?> index(@Valid IndexPostRequest request, OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return this.postService.findAll(null, request.getPage(), request.getSize(), request.getPostFilter());
        }
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return this.postService.findAll(principal.getId(), request.getPage(), request.getSize(), request.getPostFilter());
    }

    @GetMapping("{postId}")
    public ResponseEntity<?> show(@PathVariable Long postId, OAuth2AuthenticationToken authentication) {
        Optional<PostDTO> postOptional;
        if (authentication == null) {
            postOptional = this.postService.findById(postId, null);
        } else {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            postOptional = this.postService.findById(postId, principal.getId());
        }

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(postOptional.get());
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<?> delete(@PathVariable Long postId, OAuth2AuthenticationToken authentication) {
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            if (!this.postService.exists(postId, principal.getId())) {
                return ResponseEntity.badRequest().build();
            }
            this.postService.delete(postId);

            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{postId}/comment")
    public ResponseEntity<?> indexComments(@Valid IndexPostCommentsRequest request, @PathVariable Long postId) {
        try {
            if (!this.postService.exists(postId)) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(this.commentService.findAll(postId, request.getPage(), request.getSize()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("{postId}/comment")
    public ResponseEntity<?> saveComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreatePostCommentRequest request,
            OAuth2AuthenticationToken authentication
    ) {
        try {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            if (!this.postService.exists(postId)) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(this.commentService.create(postId, principal.getId(), request.getValue()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            OAuth2AuthenticationToken authentication,
            @Valid @ModelAttribute CreatePostRequest request
    ) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User          user          = userService.findByEmail(userPrincipal.getEmail()).get();

        Optional<Game> gameOptional = gameService.findById(request.getGameId());
        if (gameOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Game not found"));
        }
        Game game = gameOptional.get();
        Long imageId;
        try {
            byte[] imageBytes = request.getImage().getBytes();
            logger.info("Uploading post image for user {}", user.getId());
            imageId = imageService.create(imageBytes);
        } catch (IOException | FailedToCreateImageException e) {
            logger.error("Unable to save post image.", e);
            return ResponseEntity.internalServerError().body(Map.of("message", "Unable to save image"));
        }
        Post post = postService.create(user.getId(), game.getId(), imageId, request.getTitle(), request.getDescription());

        return ResponseEntity.ok(Map.of("id", post.getId()));
    }

    @PostMapping("{postId}/vote/{voteValue}")
    public ResponseEntity<?> vote(
            @PathVariable Long postId,
            @PathVariable Long voteValue,
            OAuth2AuthenticationToken authentication
    ) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            if (!this.postService.exists(postId)) {
                return ResponseEntity.badRequest().build();
            }

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

    @PutMapping("{postId}")
    public ResponseEntity<?> updatePost(
            @Valid @RequestBody EditPostRequest request,
            @PathVariable Long postId,
            OAuth2AuthenticationToken authentication
    ) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            if (!this.postService.exists(postId, userPrincipal.getId())) {
                return ResponseEntity.notFound().build();
            }
            Post post = new Post();
            post.setId(postId);
            post.setTitle(request.getTitle());
            post.setDescription(request.getDescription());

            this.postService.update(post);

            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
