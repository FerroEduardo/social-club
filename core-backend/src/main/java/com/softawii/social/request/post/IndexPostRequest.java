package com.softawii.social.request.post;

import com.softawii.social.repository.PostRepository;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class IndexPostRequest {

    @PositiveOrZero(message = "\"page\" must be at least 0")
    @NotNull(message = "\"page\" is required")
    private Integer page;

    @Positive(message = "\"size\" must be at least 1")
    @NotNull(message = "\"size\" is required")
    private Integer size;

    private String                    filter;
    private PostRepository.PostFilter postFilter;

    public IndexPostRequest() {
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

    public void setFilter(String filter) {
        if (filter == null) {
            this.filter = null;
            this.postFilter = null;
        } else {
            this.filter = filter;
            this.postFilter = PostRepository.PostFilter.fromName(filter).orElse(null);
        }
    }

    public PostRepository.PostFilter getPostFilter() {
        return postFilter;
    }

    @AssertTrue(message = "\"filter\" is invalid")
    public boolean isValidFilter() {
        if (filter == null) {
            return true;
        }

        return this.postFilter != null;
    }
}
