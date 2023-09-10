package com.softawii.social.model;

public class User {
    private Long id;
    private String name;
    private String email;
    private Long imageId;

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

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageUrl) {
        this.imageId = imageUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageId + '\'' +
                '}';
    }
}