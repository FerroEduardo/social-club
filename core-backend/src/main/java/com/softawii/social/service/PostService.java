package com.softawii.social.service;

import com.softawii.social.model.Post;
import com.softawii.social.model.dto.PostDTO;
import com.softawii.social.repository.CommentRepository;
import com.softawii.social.repository.ImageRepository;
import com.softawii.social.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostService {
    private final Logger            logger = LoggerFactory.getLogger(PostService.class);
    private final PostRepository    postRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository   imageRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.imageRepository = imageRepository;
    }

    public Optional<PostDTO> findById(Long id, Long userId) {
        return postRepository.findByPostId(id, userId);
    }

    public Page<PostDTO> findAll(Long userId, int page, int size, PostRepository.PostFilter filter) {
        return postRepository.findAllActive(page, size, userId, filter);
    }

    public Page<PostDTO> findUserPosts(Long userId, int page, int size) {
        return postRepository.findAllActiveByUser(page, size, userId);
    }

    public Page<PostDTO> findAllByGameId(int page, int size, Long authenticatedUserId, Long gameId, PostRepository.PostFilter postFilter) {
        return postRepository.findAllActiveByGame(page, size, authenticatedUserId, gameId, postFilter);
    }

    public Page<PostDTO> findUserVotedPosts(int page, int size, Long authenticatedUserId) {
        return postRepository.findAllByUserVote(page, size, authenticatedUserId);
    }

    public Post create(Long userId, Long gameId, Long imageId, String title, String description) {
        Post post = new Post();
        post.setUserId(userId);
        post.setGameId(gameId);
        post.setTitle(title);
        post.setDescription(description);
        post.setImageId(imageId);

        logger.info("Creating post: {}", post);
        return postRepository.save(post);
    }

    public boolean exists(Long postId, Long userId) {
        return postRepository.existsActive(postId, userId);
    }

    public boolean exists(Long postId) {
        return postRepository.existsActive(postId);
    }

    public void delete(Long postId) {
        logger.info("Deleting post {} and related content", postId);
        postRepository.softDelete(postId);
        imageRepository.softDeleteByPostId(postId);
        commentRepository.softDeleteByPostId(postId);
    }

    public void update(Post post) {
        logger.info("Updating post {}: {}", post.getId(), post);
        postRepository.update(post);
    }
}
