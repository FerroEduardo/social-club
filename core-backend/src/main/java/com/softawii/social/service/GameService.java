package com.softawii.social.service;

import com.softawii.social.model.Game;
import com.softawii.social.repository.GameRepository;
import com.softawii.social.util.Unpaged;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<Game> findAll(String name) {
        return repository.findAll(name, Unpaged.UNSORTED);
    }

    public Page<Game> findAll(int page, int size, String name) {
        return repository.findAll(name, PageRequest.of(page, size));
    }

    public Game save(String name, String studio) {
        return repository.create(new Game(name, studio));
    }

    public Game update(Long id, String name, String studio) {
        Optional<Game> optionalGame = repository.findById(id);
        Game           game         = optionalGame.orElseThrow();
        game.setName(name);
        game.setStudio(studio);

        return repository.update(game);
    }

    public void remove(Long id) throws NoSuchElementException {
        repository.findById(id).orElseThrow();
        repository.delete(id);
    }
}
