package com.softawii.social.model;

import com.softawii.social.model.embeddable.PostVoteId;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.ZonedDateTime;

@Entity
@Table(name = "post_vote")
@DynamicInsert
@IdClass(PostVoteId.class)
public class PostVote {
    @Id
    private Long postId;

    @Id
    private Long userId;

    @Column(name = "value")
    private Long value;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;

    public PostVote() {
    }

    public PostVote(PostVoteId postVoteId, Long value, ZonedDateTime createdAt, ZonedDateTime modifiedAt) {
        this.postId = postVoteId.getPostId();
        this.userId = postVoteId.getUserId();
        this.value = value;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
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
