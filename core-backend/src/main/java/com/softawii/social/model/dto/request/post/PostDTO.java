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

    public PostDTO(Long id, String description, ZonedDateTime createdAt, ZonedDateTime modifiedAt, Long authorId, String authorName, String authorImageUrl, Long gameId, String gameName, String gameStudio, String imageUrl) {
        this.id = id;
        this.description = description;
        this.reputation = null;
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getReputation() {
        return reputation;
    }

    public void setReputation(Long reputation) {
        this.reputation = reputation;
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

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameStudio() {
        return gameStudio;
    }

    public void setGameStudio(String gameStudio) {
        this.gameStudio = gameStudio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
