package com.example.bookstore.model;

public class Author {
    private Long id;
    private String name;
    private String slug;
    private String biography;
    private String country;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
