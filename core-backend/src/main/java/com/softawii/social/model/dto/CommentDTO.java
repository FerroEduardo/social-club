package com.softawii.social.model.dto;

import java.time.ZonedDateTime;

public class CommentDTO {
    private Long          id;
    private Long          authorId;
    private String        authorName;
    private String        value;
    private ZonedDateTime createdAt;
    private String        authorAvatarUrl;
    private String        authorMiniAvatarUrl;

    public CommentDTO() {
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

    public String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }

    public void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    public String getAuthorMiniAvatarUrl() {
        return authorMiniAvatarUrl;
    }

    public void setAuthorMiniAvatarUrl(String authorMiniAvatarUrl) {
        this.authorMiniAvatarUrl = authorMiniAvatarUrl;
    }
}
