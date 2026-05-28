package com.example.bookstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Book {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String sku;
    private String isbn13;
    private String title;
    private String description;
    private String publisher;
    private Integer publicationYear;
    private String language;
    private BigDecimal listPrice;
    private Integer stockOnHand;
    private Integer reorderLevel;
    private String status;
    private String imageUrl;
    private LocalDateTime createdAt;
    private String authors;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getIsbn13() { return isbn13; }
    public void setIsbn13(String isbn13) { this.isbn13 = isbn13; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public BigDecimal getListPrice() { return listPrice; }
    public void setListPrice(BigDecimal listPrice) { this.listPrice = listPrice; }
    public Integer getStockOnHand() { return stockOnHand; }
    public void setStockOnHand(Integer stockOnHand) { this.stockOnHand = stockOnHand; }
    public Integer getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(Integer reorderLevel) { this.reorderLevel = reorderLevel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getAuthors() { return authors; }
    public void setAuthors(String authors) { this.authors = authors; }
}
