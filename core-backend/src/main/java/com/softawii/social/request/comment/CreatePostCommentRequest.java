package com.softawii.social.request.comment;

import jakarta.validation.constraints.Size;

public class CreatePostCommentRequest {
    @Size(min = 1, max = 200, message = "The length of \"value\" must be between 1 and 200")
    private String value;

    public CreatePostCommentRequest() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
