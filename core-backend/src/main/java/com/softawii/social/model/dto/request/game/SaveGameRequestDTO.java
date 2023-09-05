package com.softawii.social.model.dto.request.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SaveGameRequestDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "The length of \"name\" must be between 1 and 100")
    private String name;

    @NotBlank(message = "Studio is required")
    @Size(min = 1, max = 100, message = "The length of \"studio\" must be between 1 and 100")
    private String studio;

    @NotBlank(message = "Image URL is required")
    @Size(min = 1, max = 100, message = "The length of \"imageUrl\" must be between 1 and 100")
    private String imageUrl;

    public SaveGameRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
