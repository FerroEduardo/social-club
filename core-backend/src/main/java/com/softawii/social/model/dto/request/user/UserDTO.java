package com.softawii.social.model.dto.request.user;

import com.softawii.social.model.User;
import com.softawii.social.service.ImageService;

public class UserDTO {
    private Long   id;
    private String name;
    private String email;
    private String imageUrl;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String email, String imageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), ImageService.getImageUrlFromImageId(user.getImageId()));
    }
}
