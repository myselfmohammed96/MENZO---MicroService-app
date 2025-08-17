package com.menzo.Admin_Service.Dto;

public class ProductMinimalDto {

    private Long productId;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private Long subCategoryId;
    private String subCategoryName;

    public ProductMinimalDto() {}

    public ProductMinimalDto(Long productId, String productName,
                             Long categoryId, String categoryName,
                             Long subCategoryId, String subCategoryName) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public void display() {
        System.out.println("ProductMinimalDto:\nid: " + productId + "\nproductName: " + productName +
                "\ncategoryId: " + categoryId + "\ncategoryName: " + categoryName +
                "\nsubCategoryId: " + subCategoryId + "\nsubCategoryName: " + subCategoryName);
    }
}
