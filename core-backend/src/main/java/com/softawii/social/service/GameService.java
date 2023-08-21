package com.softawii.social.service;

import com.softawii.social.model.Game;
import com.softawii.social.repository.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class GameService {

    private final GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public Optional<Game> findById(Long id) {
        return repository.findById(id);
    }

    public Page<Game> findAll() {
        return repository.findAll(Pageable.unpaged());
    }

    public Page<Game> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public Game save(String name, String studio) {
        return repository.save(new Game(name, studio));
    }

    public Game update(Long id, String name, String studio) {
        Optional<Game> optionalGame = repository.findById(id);
        Game           game         = optionalGame.orElseThrow();
        game.setName(name);
        game.setStudio(studio);

        return repository.save(game);
    }

    public void remove(Long id) throws NoSuchElementException {
        Optional<Game> optionalGame = repository.findById(id);
        Game           game         = optionalGame.orElseThrow();
        repository.delete(game);
    }
}
