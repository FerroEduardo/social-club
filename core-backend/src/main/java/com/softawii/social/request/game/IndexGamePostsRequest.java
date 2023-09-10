package com.softawii.social.request.game;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class IndexGamePostsRequest {

    @PositiveOrZero(message = "\"page\" must be at least 0")
    private Integer page;

    @Positive(message = "\"size\" must be at least 1")
    private Integer size;

    public IndexGamePostsRequest() {
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
