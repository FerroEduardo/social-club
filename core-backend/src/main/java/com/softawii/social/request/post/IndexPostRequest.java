package com.softawii.social.request.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class IndexPostRequest {

    @PositiveOrZero(message = "\"page\" must be at least 0")
    @NotNull(message = "\"page\" is required")
    private Long page;

    @Positive(message = "\"size\" must be at least 1")
    @NotNull(message = "\"size\" is required")
    private Long size;

    public IndexPostRequest() {
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
