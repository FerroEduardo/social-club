package com.softawii.social.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class Unpaged implements Pageable {

    public static final Pageable UNSORTED = new Unpaged(Sort.unsorted());

    private final int  pageSize;
    private final Sort sort;

    Unpaged(Sort sort) {
        this.sort = sort;
        this.pageSize = Integer.MAX_VALUE;
    }

    static Pageable sorted(Sort sort) {
        return sort.isSorted() ? new Unpaged(sort) : UNSORTED;
    }

    @Override
    public boolean isPaged() {
        return false;
    }

    @Override
    public Pageable previousOrFirst() {
        return this;
    }

    @Override
    public Pageable next() {
        return this;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public long getOffset() {
        return 0;
    }

    @Override
    public Pageable first() {
        return this;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return this;
    }
}