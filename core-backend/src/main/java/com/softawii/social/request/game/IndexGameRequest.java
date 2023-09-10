package com.softawii.social.request.game;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class IndexGameRequest {

    @PositiveOrZero(message = "\"page\" must be at least 0")
    private Long page;

    @Positive(message = "\"size\" must be at least 1")
    private Long size;

    private String name;

    public IndexGameRequest() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
