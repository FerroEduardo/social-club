package com.softawii.social.model;

import java.time.ZonedDateTime;

public class PostVote {
    private Long postId;
    private Long userId;
    private Long value;
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;

    public PostVote() {
    }

    public PostVote(Long postId, Long userId, Long value, ZonedDateTime createdAt, ZonedDateTime modifiedAt) {
        this.postId = postId;
        this.userId = userId;
        this.value = value;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
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

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
