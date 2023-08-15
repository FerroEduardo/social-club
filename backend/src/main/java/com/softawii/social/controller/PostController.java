package com.softawii.social.controller;

import com.softawii.social.config.AppConfig;
import com.softawii.social.model.Game;
import com.softawii.social.model.Image;
import com.softawii.social.model.Post;
import com.softawii.social.model.User;
import com.softawii.social.model.dto.post.CreatePostDTO;
import com.softawii.social.model.dto.post.IndexPostDTO;
import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.GameService;
import com.softawii.social.service.ImageService;
import com.softawii.social.service.PostService;
import com.softawii.social.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "post")
@Validated
public class PostController {

    private final UserService  userService;
    private final GameService  gameService;
    private final PostService  postService;
    private final ImageService imageService;
    private final AppConfig    appConfig;

    public PostController(UserService userService, GameService gameService, PostService postService, ImageService imageService, AppConfig appConfig) {
        this.userService = userService;
        this.gameService = gameService;
        this.postService = postService;
        this.imageService = imageService;
        this.appConfig = appConfig;
    }

    @GetMapping
    public Iterable<?> index(@Valid IndexPostDTO dto) {
        if (dto.isPaginated()) {
            return this.postService.findAll(dto.getPage().intValue(), dto.getSize().intValue());
        }

        return this.postService.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            Post post = this.postService.findById(id).orElseThrow();

            return ResponseEntity.ok(post);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            OAuth2AuthenticationToken authentication,
            @Valid @ModelAttribute CreatePostDTO dto
    ) throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User          user          = userService.findByEmail(userPrincipal.getEmail()).get();

        Optional<Game> gameOptional = gameService.findById(dto.getGameId());
        if (gameOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found");
        }
        Game  game = gameOptional.get();
        Image image;
        try {
            byte[] imageBytes = dto.getImage().getBytes(); // throws IOException
            image = imageService.create(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Unable to save image");
        }
        Post post = postService.create(user, game, image, dto.getDescription());

        return ResponseEntity.ok(post);
    }
}
