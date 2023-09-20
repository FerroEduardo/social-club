package com.softawii.social.model.dto;

public class UserDTO {
    private Long   id;
    private String name;
    private String email;
    private String avatarUrl;
    private String miniAvatarUrl;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String email, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getMiniAvatarUrl() {
        return miniAvatarUrl;
    }

    public void setMiniAvatarUrl(String miniAvatarUrl) {
        this.miniAvatarUrl = miniAvatarUrl;
    }
}
