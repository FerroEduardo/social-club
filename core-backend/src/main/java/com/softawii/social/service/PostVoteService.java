package com.softawii.social.service;

import com.softawii.social.model.PostVote;
import com.softawii.social.repository.PostVoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostVoteService {

    private final Logger             logger = LoggerFactory.getLogger(PostVoteService.class);
    private final PostVoteRepository repository;

    public PostVoteService(PostVoteRepository repository) {
        this.repository = repository;
    }

    public Long getPostReputation(Long postId) {
        return repository.getReputationByPostId(postId);
    }

    public void vote(Long postId, Long userId, Long value) {
        Optional<PostVote> optionalPostVote = repository.findById(userId, postId);
        PostVote           postVote;
        if (optionalPostVote.isPresent()) {
            postVote = optionalPostVote.get();
            postVote.setValue(value);
            repository.update(postVote);
        } else {
            repository.create(new PostVote(postId, userId, value, null, null));
        }
        logger.info("User {} voted {} for post {}", userId, value, postId);
    }
}
