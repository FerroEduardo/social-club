package com.softawii.social.model.dto.request.post;

import java.time.ZonedDateTime;

public class PostDTO {
    private Long          id;
    private String        description;
    private Long          reputation;
    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;
    private Long          authorId;
    private String        authorName;
    private String        authorImageUrl;
    private Long          gameId;
    private String        gameName;
    private String        gameStudio;
    private String        imageUrl;
    private Short         userVote;

    public PostDTO() {
    }

    public Long getId() {
        return id;
    }

    public PostDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PostDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getReputation() {
        return reputation;
    }

    public PostDTO setReputation(Long reputation) {
        this.reputation = reputation;
        return this;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public PostDTO setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public PostDTO setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public PostDTO setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public PostDTO setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public PostDTO setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
        return this;
    }

    public Long getGameId() {
        return gameId;
    }

    public PostDTO setGameId(Long gameId) {
        this.gameId = gameId;
        return this;
    }

    public String getGameName() {
        return gameName;
    }

    public PostDTO setGameName(String gameName) {
        this.gameName = gameName;
        return this;
    }

    public String getGameStudio() {
        return gameStudio;
    }

    public PostDTO setGameStudio(String gameStudio) {
        this.gameStudio = gameStudio;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public PostDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Short getUserVote() {
        return userVote;
    }

    public PostDTO setUserVote(Short userVote) {
        this.userVote = userVote;
        return this;
    }
}
