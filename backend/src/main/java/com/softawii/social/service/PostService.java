package com.softawii.social.service;

import com.softawii.social.model.Game;
import com.softawii.social.model.Image;
import com.softawii.social.model.Post;
import com.softawii.social.model.User;
import com.softawii.social.model.dto.PostDTO;
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

    public Optional<PostDTO> findById(Long id) {
        return repository.findPosts("http://localhost:8099/image/", id, PageRequest.of(0, 1)).stream().findFirst();
    }

    public Page<PostDTO> findAll() {
        return repository.findPosts("http://localhost:8099/image/", null, Pageable.unpaged());
    }

    public Page<PostDTO> findAll(int page, int size) {
        return repository.findPosts("http://localhost:8099/image/", null, PageRequest.of(page, size, Sort.by("createdAt")));
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
