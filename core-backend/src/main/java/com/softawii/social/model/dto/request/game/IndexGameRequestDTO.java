package com.softawii.social.model.dto.request.game;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class IndexGameRequestDTO {

    @PositiveOrZero(message = "\"page\" must be at least 0")
    private Long page;

    @Positive(message = "\"size\" must be at least 1")
    private Long size;

    private String name;

    public IndexGameRequestDTO() {
    }

    @AssertTrue(message = "Fields must be null or totally filled")
    private boolean isValid() {
        return !(page != null ^ size != null) && (name == null || !name.isBlank());
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

    public boolean isPaginated() {
        return page != null && size != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
