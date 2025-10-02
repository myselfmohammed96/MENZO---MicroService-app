package com.menzo.Home_Service.Dto;

import java.time.Instant;

public class ParentCategoryDto {
    private Long id;
    private String categoryName;
    private Boolean isActive;
    private Instant createdAt;

    public ParentCategoryDto(){}

    public ParentCategoryDto(Long id, String categoryName, Boolean isActive, Instant createdAt){
        this.id = id;
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void display() {
        System.out.println("ParentCategoryDto:\nid: " + id + "\ncategoryName: " + categoryName +
                "\nisActive: " + isActive + "\ncreatedAt: " + createdAt);
    }
}
