package com.softawii.social.service;

import com.softawii.social.model.Game;
import com.softawii.social.model.Image;
import com.softawii.social.model.Post;
import com.softawii.social.model.User;
import com.softawii.social.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Optional<Post> findById(Long id) {
        return repository.findById(id);
    }

    public Page<Post> findAll() {
        return repository.findAll(Pageable.unpaged());
    }

    public Page<Post> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size, Sort.by("")));
    }

    public Post create(User user, Game game, Image image, String description) {
        Post post = new Post();
        post.setUser(user);
        post.setGame(game);
        post.setDescription(description);
        post.setImage(image);

        return repository.save(post);
    }
}
