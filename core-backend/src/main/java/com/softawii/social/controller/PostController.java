package com.softawii.social.controller;

import com.softawii.social.exception.FailedToCreateImageException;
import com.softawii.social.model.Game;
import com.softawii.social.model.Image;
import com.softawii.social.model.Post;
import com.softawii.social.model.User;
import com.softawii.social.model.dto.request.post.CreatePostRequestDTO;
import com.softawii.social.model.dto.request.post.IndexPostRequestDTO;
import com.softawii.social.model.dto.request.post.PostDTO;
import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.*;
import jakarta.validation.Valid;
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

    private final UserService     userService;
    private final GameService     gameService;
    private final PostService     postService;
    private final ImageService    imageService;
    private final PostVoteService postVoteService;

    public PostController(UserService userService, GameService gameService, PostService postService, ImageService imageService, PostVoteService postVoteService) {
        this.userService = userService;
        this.gameService = gameService;
        this.postService = postService;
        this.imageService = imageService;
        this.postVoteService = postVoteService;
    }

    @GetMapping
    public Iterable<?> index(@Valid IndexPostRequestDTO dto, OAuth2AuthenticationToken authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if (dto.isPaginated()) {
            return this.postService.findAll(principal.getId(), dto.getPage().intValue(), dto.getSize().intValue());
        }

        return this.postService.findAll(principal.getId());
    }

    @GetMapping("{postId}")
    public ResponseEntity<?> show(@PathVariable Long postId, OAuth2AuthenticationToken authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        try {
            PostDTO post = this.postService.findById(postId, principal.getId()).orElseThrow();

            return ResponseEntity.ok(post);
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
        Post post = postService.create(user, game, image, dto.getDescription());

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
            if (voteValue == 1 || voteValue == -1 || voteValue == 0) {
                this.postService.findById(postId).orElseThrow();
                long userId = userPrincipal.getId();

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
