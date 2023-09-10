package com.softawii.social.service;

import com.softawii.social.model.Game;
import com.softawii.social.repository.GameRepository;
import com.softawii.social.repository.PostRepository;
import com.softawii.social.util.Unpaged;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class GameService {

    private final GameRepository gameRepository;
    private final PostRepository postRepository;

    public GameService(GameRepository gameRepository, PostRepository postRepository) {
        this.gameRepository = gameRepository;
        this.postRepository = postRepository;
    }

    public Optional<Game> findById(Long id) {
        return gameRepository.findById(id);
    }

    public Page<Game> findAll(String name) {
        return gameRepository.findAll(name, Unpaged.UNSORTED);
    }

    public Page<Game> findAll(int page, int size, String name) {
        return gameRepository.findAll(name, PageRequest.of(page, size));
    }
    public Game save(String name, String studio, String imageUrl) {
        return gameRepository.create(new Game(name, studio, imageUrl));
    }

    public Game update(Long id, String name, String studio) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        Game           game         = optionalGame.orElseThrow();
        game.setName(name);
        game.setStudio(studio);

        return gameRepository.update(game);
    }

    public void remove(Long id) throws NoSuchElementException {
        gameRepository.findById(id).orElseThrow();
        gameRepository.delete(id);
    }
}
