package com.softawii.social.service;

import com.softawii.social.model.Game;
import com.softawii.social.repository.GameRepository;
import com.softawii.social.repository.PostRepository;
import com.softawii.social.util.Unpaged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class GameService {

    private final Logger         logger = LoggerFactory.getLogger(GameService.class);
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
        Game game = new Game(name, studio, imageUrl);
        logger.info("Saving game: {}", game);
        return gameRepository.create(game);
    }

    public Game update(Long id, String name, String studio) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        Game           game         = optionalGame.orElseThrow();
        game.setName(name);
        game.setStudio(studio);
        logger.info("Updating game: {}", game);

        return gameRepository.update(game);
    }

    public void remove(Long id) throws NoSuchElementException {
        gameRepository.findById(id).orElseThrow();
        gameRepository.delete(id);
        logger.info("Deleting game: {}", id);
    }
}
