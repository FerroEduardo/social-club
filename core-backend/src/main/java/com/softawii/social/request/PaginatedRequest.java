package com.softawii.social.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class PaginatedRequest {

    @PositiveOrZero(message = "\"page\" must be at least 0")
    @NotNull(message = "\"page\" is required")
    private Integer page;

    @Positive(message = "\"size\" must be at least 1")
    @NotNull(message = "\"size\" is required")
    private Integer size;

    public PaginatedRequest() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
