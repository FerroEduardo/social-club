package com.softawii.social.model.embeddable;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PostVoteId implements Serializable {
    private Long postId;
    private Long userId;

    public PostVoteId() {
    }

    public PostVoteId(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
