package com.softawii.social.controller;

import com.softawii.social.model.Game;
import com.softawii.social.model.dto.PostDTO;
import com.softawii.social.request.game.IndexGamePostsRequest;
import com.softawii.social.request.game.IndexGameRequest;
import com.softawii.social.request.game.SaveGameRequest;
import com.softawii.social.request.game.UpdateGameRequest;
import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.GameService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/game")
@Validated
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<?>> index(@Valid IndexGameRequest request) {
        return ResponseEntity.ok(this.service.findAll(request.getPage().intValue(), request.getSize().intValue(), request.getName()));
    }

    @GetMapping("{gameId}")
    public ResponseEntity<Game> show(@Valid @PathVariable @NotNull @PositiveOrZero Long gameId) {
        Optional<Game> optionalGame = this.service.findById(gameId);
        if (optionalGame.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalGame.get());
    }

    @GetMapping("{gameId}/post")
    public ResponseEntity<Page<PostDTO>> indexPosts(
            @Valid @PathVariable @NotNull @PositiveOrZero Long gameId,
            @Valid IndexGamePostsRequest request,
            OAuth2AuthenticationToken authentication
    ) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(this.service.findPostsByGameId(request.getPage(), request.getSize(), user.getId(), gameId));
    }

    @PostMapping
    public Game store(@Valid @RequestBody SaveGameRequest gameRequest) {
        return this.service.save(gameRequest.getName(), gameRequest.getStudio(), gameRequest.getImageUrl());
    }

    @PutMapping("{id}")
    public ResponseEntity<Game> update(
            @Valid @PathVariable @NotNull @PositiveOrZero Long id,
            @Valid @RequestBody UpdateGameRequest gameRequest
    ) {
        try {
            Game updatedGame = this.service.update(id, gameRequest.getName(), gameRequest.getStudio());
            return ResponseEntity.ok(updatedGame);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> remove(@Valid @PathVariable @NotNull @PositiveOrZero Long id) {
        try {
            this.service.remove(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
