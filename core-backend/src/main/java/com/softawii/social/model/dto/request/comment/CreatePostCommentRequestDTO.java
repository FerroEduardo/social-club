package com.softawii.social.model.dto.request.comment;

import jakarta.validation.constraints.Size;

public class CreatePostCommentRequestDTO {
    @Size(min = 1, max = 200, message = "The length of \"value\" must be between 1 and 200")
    private String value;

    public CreatePostCommentRequestDTO() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
