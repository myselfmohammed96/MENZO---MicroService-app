package com.menzo.Product_Service.Dto.CategoriesDto;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.*;

public class CreateSubCategoryDto {

    private Long id;
    @NotNull(message = "Parent category ID is required")
    private Long parentCategoryId;
    @NotBlank(message = "Category name is required")
    private String categoryName;
    private Set<Long> variationIds = new HashSet<>();
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

    public CreateSubCategoryDto(Long id, Long parentCategoryId, String categoryName, Set<Long> variationIds, Boolean isActive, Instant createdAt){
        this.id = id;
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
        this.variationIds = variationIds;
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

    public Set<Long> getVariationIds() {
        return variationIds;
    }

    public void setVariationIds(Set<Long> variationIds) {
        this.variationIds = variationIds;
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
        System.out.println("CreateSubCategoryDto:\nid: " + id + "\nparentCategoryId: " + parentCategoryId +
                "\ncategoryName: " + categoryName + "\nisActive: " + isActive + "\ncreatedAt: " + createdAt);
    }
}
