package com.softawii.social.request.post;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class IndexPostCommentsRequest {
    @PositiveOrZero(message = "\"page\" must be at least 0")
    private Integer page;

    @Positive(message = "\"size\" must be at least 1")
    private Integer size;

    public IndexPostCommentsRequest() {
    }

    @AssertTrue(message = "Fields must be null or totally filled")
    private boolean isValid() {
        return !(page != null ^ size != null);
    }

    public @PositiveOrZero(message = "\"page\" must be at least 0") Integer getPage() {
        return page;
    }

    public void setPage(@PositiveOrZero(message = "\"page\" must be at least 0") Integer page) {
        this.page = page;
    }

    public @Positive(message = "\"size\" must be at least 1") Integer getSize() {
        return size;
    }

    public void setSize(@Positive(message = "\"size\" must be at least 1") Integer size) {
        this.size = size;
    }

    public boolean isPaginated() {
        return page != null && size != null;
    }
}
