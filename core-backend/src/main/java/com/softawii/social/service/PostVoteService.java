package com.softawii.social.service;

import com.softawii.social.model.PostVote;
import com.softawii.social.model.embeddable.PostVoteId;
import com.softawii.social.repository.PostVoteRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostVoteService {
    private final PostVoteRepository repository;

    public PostVoteService(PostVoteRepository repository) {
        this.repository = repository;
    }

    public Long getPostReputation(Long postId) {
        return repository.getReputationByPostId(postId);
    }

    public void vote(Long postId, Long userId, Long value) {
        PostVoteId         postVoteId       = new PostVoteId(postId, userId);
        Optional<PostVote> optionalPostVote = repository.findById(postVoteId);
        PostVote           postVote;
        if (optionalPostVote.isPresent()) {
            postVote = optionalPostVote.get();
        } else {
            postVote = repository.save(new PostVote(postVoteId, value, null, null));
        }

        postVote.setValue(value);
        repository.save(postVote);
    }
}
