package com.softawii.social.controller;

import com.softawii.social.model.Game;
import com.softawii.social.model.dto.game.IndexGameDTO;
import com.softawii.social.model.dto.game.SaveGameDTO;
import com.softawii.social.model.dto.game.UpdateGameDTO;
import com.softawii.social.service.GameService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/game")
@Validated
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<Game> index(@Valid IndexGameDTO dto) {
        if (dto.isPaginated()) {
            return this.service.findAll(dto.getPage().intValue(), dto.getSize().intValue());
        }

        return this.service.findAll();
    }

    @PostMapping
    public Game store(@Valid @RequestBody SaveGameDTO game) {
        return this.service.save(game.getName(), game.getStudio());
    }

    @PutMapping("{id}")
    public ResponseEntity<Game> update(
            @Valid @PathVariable @NotNull @PositiveOrZero Long id,
            @Valid @RequestBody UpdateGameDTO dto
    ) {
        try {
            Game updatedGame = this.service.update(id, dto.getName(), dto.getStudio());
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
