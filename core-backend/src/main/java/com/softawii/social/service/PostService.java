package com.softawii.social.service;

import com.softawii.social.model.Game;
import com.softawii.social.model.Image;
import com.softawii.social.model.Post;
import com.softawii.social.model.User;
import com.softawii.social.model.dto.request.post.PostDTO;
import com.softawii.social.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Optional<PostDTO> findById(Long id, Long userId) {
        return repository.findByIdSafe(id, userId);
    }

    public Page<PostDTO> findAll(Long userId, int page, int size) {
        return repository.findAllSafe(page, size, userId);
    }

    public Page<PostDTO> findAll(int page, int size) {
        return this.findAll(null, page, size);
    }

    public Number create(User user, Game game, Image image, String title, String description) {
        Post post = new Post();
        post.setUser(user);
        post.setGame(game);
        post.setTitle(title);
        post.setDescription(description);
        post.setImage(image);

        return repository.save(post);
    }
}
