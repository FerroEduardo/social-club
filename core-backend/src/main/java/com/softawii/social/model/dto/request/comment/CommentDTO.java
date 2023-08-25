package com.softawii.social.model.dto.request.comment;

import java.time.ZonedDateTime;

public class CommentDTO {
    private Long          id;
    private Long          authorId;
    private String        authorName;
    private String        value;
    private ZonedDateTime createdAt;
    private String        authorImageUrl;

    public CommentDTO() {
    }

    public CommentDTO(Long id, Long authorId, String authorName, String value, ZonedDateTime createdAt, String authorImageUrl) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.value = value;
        this.createdAt = createdAt;
        this.authorImageUrl = authorImageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }
}
