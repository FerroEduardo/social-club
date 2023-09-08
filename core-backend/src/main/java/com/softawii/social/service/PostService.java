package com.softawii.social.service;

import com.softawii.social.model.Game;
import com.softawii.social.model.Image;
import com.softawii.social.model.Post;
import com.softawii.social.model.User;
import com.softawii.social.model.dto.request.post.PostDTO;
import com.softawii.social.repository.CommentRepository;
import com.softawii.social.repository.ImageRepository;
import com.softawii.social.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostService {
    private final PostRepository    postRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository   imageRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.imageRepository = imageRepository;
    }

    public Optional<PostDTO> findById(Long id, Long userId) {
        return postRepository.findByIdSafe(id, userId);
    }

    public Page<PostDTO> findAll(Long userId, int page, int size) {
        return postRepository.findAllDTOActive(page, size, userId);
    }

    public Page<PostDTO> findUserPosts(Long userId, int page, int size) {
        return postRepository.findAllActiveByUser(page, size, userId);
    }

    public Page<PostDTO> findAll(int page, int size) {
        return this.findAll(null, page, size);
    }

    public Post create(User user, Game game, Image image, String title, String description) {
        Post post = new Post();
        post.setUser(user);
        post.setGame(game);
        post.setTitle(title);
        post.setDescription(description);
        post.setImage(image);

        return postRepository.save(post);
    }

    public boolean exists(Long postId, Long userId) {
        return postRepository.existsActive(postId, userId);
    }

    public boolean exists(Long postId) {
        return postRepository.existsActive(postId);
    }

    public void delete(Long postId) {
        postRepository.softDelete(postId);
        imageRepository.softDeleteByPostId(postId);
        commentRepository.softDeleteByPostId(postId);
    }
}
