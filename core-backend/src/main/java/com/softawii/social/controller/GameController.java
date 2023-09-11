package com.softawii.social.controller;

import com.softawii.social.model.Game;
import com.softawii.social.model.dto.PostDTO;
import com.softawii.social.request.game.IndexGameRequest;
import com.softawii.social.request.post.IndexPostRequest;
import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.GameService;
import com.softawii.social.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/game")
@Validated
public class GameController {

    private final GameService gameService;
    private final PostService postService;

    public GameController(GameService gameService, PostService postService) {
        this.gameService = gameService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Page<?>> index(@Valid IndexGameRequest request) {
        return ResponseEntity.ok(this.gameService.findAll(request.getPage().intValue(), request.getSize().intValue(), request.getName()));
    }

    @GetMapping("{gameId}")
    public ResponseEntity<Game> show(@Valid @PathVariable @NotNull @PositiveOrZero Long gameId) {
        Optional<Game> optionalGame = this.gameService.findById(gameId);
        if (optionalGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalGame.get());
    }

    @GetMapping("{gameId}/post")
    public ResponseEntity<Page<PostDTO>> indexPosts(
            @Valid @PathVariable @NotNull @PositiveOrZero Long gameId,
            @Valid IndexPostRequest request,
            OAuth2AuthenticationToken authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.ok(this.postService.findAllByGameId(request.getPage(), request.getSize(), null, gameId, request.getPostFilter()));
        }

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(this.postService.findAllByGameId(request.getPage(), request.getSize(), user.getId(), gameId, request.getPostFilter()));
    }

//    @PostMapping
//    public Game store(@Valid @RequestBody SaveGameRequest gameRequest) {
//        return this.gameService.save(gameRequest.getName(), gameRequest.getStudio(), gameRequest.getImageUrl());
//    }
//
//    @PutMapping("{id}")
//    public ResponseEntity<Game> update(
//            @Valid @PathVariable @NotNull @PositiveOrZero Long id,
//            @Valid @RequestBody UpdateGameRequest gameRequest
//    ) {
//        try {
//            Game updatedGame = this.gameService.update(id, gameRequest.getName(), gameRequest.getStudio());
//            return ResponseEntity.ok(updatedGame);
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("{id}")
//    public ResponseEntity<Object> remove(@Valid @PathVariable @NotNull @PositiveOrZero Long id) {
//        try {
//            this.gameService.remove(id);
//            return ResponseEntity.noContent().build();
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
