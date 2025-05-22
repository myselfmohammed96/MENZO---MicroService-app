package com.menzo.Product_Service.Dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class CreateParentCategoryDto {

    private Long id;
    @NotBlank(message = "Category name is required")
    private String categoryName;
    private Boolean isActive;
    private Instant createdAt;

    public CreateParentCategoryDto(){}

    public CreateParentCategoryDto(Long id, String categoryName, Boolean isActive, Instant createdAt){
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
}
