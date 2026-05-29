package com.example.bookstore.dto;

import java.util.List;

public class PageResult<T> {
    private final List<T> items;
    private final int page;
    private final int size;
    private final long totalItems;

    public PageResult(List<T> items, int page, int size, long totalItems) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
    }

    public List<T> getItems() { return items; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalItems() { return totalItems; }
    public long getTotalPages() { return (long) Math.ceil(totalItems / (double) size); }
    public long getStartItem() { return totalItems == 0 ? 0 : (long) page * size + 1; }
    public long getEndItem() { return Math.min(totalItems, (long) (page + 1) * size); }
    public boolean hasPrevious() { return page > 0; }
    public boolean hasNext() { return page + 1 < getTotalPages(); }
}
