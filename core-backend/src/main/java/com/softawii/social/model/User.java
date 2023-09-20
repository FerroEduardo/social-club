package com.softawii.social.model;

public class User {
    private Long   id;
    private String name;
    private String email;
    private Long   avatarImageId;
    private Long   smallAvatarImageId;

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

    public Long getAvatarImageId() {
        return avatarImageId;
    }

    public void setAvatarImageId(Long avatarImageId) {
        this.avatarImageId = avatarImageId;
    }

    public Long getSmallAvatarImageId() {
        return smallAvatarImageId;
    }

    public void setSmallAvatarImageId(Long smallAvatarImageId) {
        this.smallAvatarImageId = smallAvatarImageId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", avatarImageId=" + avatarImageId +
                ", smallAvatarImageId=" + smallAvatarImageId +
                '}';
    }
}