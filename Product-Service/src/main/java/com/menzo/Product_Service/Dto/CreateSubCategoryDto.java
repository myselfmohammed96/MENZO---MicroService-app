package com.menzo.Product_Service.Dto;

import java.time.Instant;
import jakarta.validation.constraints.*;

public class CreateSubCategoryDto {

    private Long id;
    @NotNull(message = "Parent category ID is required")
    private Long parentCategoryId;
    @NotBlank(message = "Category name is required")
    private String categoryName;
    private Boolean isActive;
    private Instant createdAt;

    public CreateSubCategoryDto(){}

    public CreateSubCategoryDto(Long id, Long parentCategoryId, String categoryName, Boolean isActive, Instant createdAt){
        this.id = id;
        this.parentCategoryId = parentCategoryId;
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

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
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
