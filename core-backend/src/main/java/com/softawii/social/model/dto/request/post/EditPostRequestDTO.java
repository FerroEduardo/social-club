package com.softawii.social.model.dto.request.post;

import org.hibernate.validator.constraints.Length;

public class EditPostRequestDTO {
    @Length(min = 1, max = 100, message = "The length of \"title\" must be between 1 and 200")
    private String title;

    @Length(min = 1, max = 200, message = "The length of \"description\" must be between 1 and 200")
    private String description;

    public EditPostRequestDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null && title.isBlank()) {
            title = null;
        }
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.isBlank()) {
            description = null;
        }
        this.description = description;
    }
}
