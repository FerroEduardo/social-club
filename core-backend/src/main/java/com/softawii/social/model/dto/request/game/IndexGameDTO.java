package com.softawii.social.model.dto.request.game;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class IndexGameDTO {

    @PositiveOrZero(message = "\"page\" must be at least 0")
    private Long page;

    @Positive(message = "\"size\" must be at least 1")
    private Long size;

    public IndexGameDTO() {
    }

    @AssertTrue(message = "Fields must be null or totally filled")
    private boolean isValid() {
        return !(page != null ^ size != null);
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
}
