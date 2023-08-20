package com.softawii.social.model.dto;

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

    public PostDTO() {
    }

    public PostDTO(Long id, String description, Long reputation, ZonedDateTime createdAt, ZonedDateTime modifiedAt, Long authorId, String authorName, String authorImageUrl, Long gameId, String gameName, String gameStudio, String imageUrl) {
        this.id = id;
        this.description = description;
        this.reputation = reputation;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorImageUrl = authorImageUrl;
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameStudio = gameStudio;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Long getReputation() {
        return reputation;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameStudio() {
        return gameStudio;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
