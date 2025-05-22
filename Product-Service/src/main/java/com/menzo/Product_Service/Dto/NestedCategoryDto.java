package com.menzo.Product_Service.Dto;

import java.util.List;

public class NestedCategoryDto {

    private Long id;
    private String categoryName;
    private List<NestedCategoryDto> subCategories;

    public NestedCategoryDto(){}

    public NestedCategoryDto(Long id, String categoryName, List<NestedCategoryDto> subCategories){
        this.id = id;
        this.categoryName = categoryName;
        this.subCategories = subCategories;
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

    public List<NestedCategoryDto> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<NestedCategoryDto> subCategories) {
        this.subCategories = subCategories;
    }
}
