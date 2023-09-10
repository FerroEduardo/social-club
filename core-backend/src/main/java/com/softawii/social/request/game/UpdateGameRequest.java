package com.softawii.social.request.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateGameRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "The length of \"name\" must be between 1 and 100")
    private String name;

    @NotBlank(message = "Studio is required")
    @Size(min = 1, max = 100, message = "The length of \"studio\" must be between 1 and 100")
    private String studio;

    public UpdateGameRequest() {
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

    @Override
    public String toString() {
        return "UpdateGameDTO{" +
                "name='" + name + '\'' +
                ", studio='" + studio + '\'' +
                '}';
    }
}
