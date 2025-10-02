package com.menzo.Product_Service.Dto.ProductDto;

import java.util.*;

public class ProductItemDetailsDto {

    private Long productItemId;
    private String productName;
    private String categoryName;
    private String subCategoryName;
    private String productDescription;
    private boolean podAvailable;
    private Date addedDate;

    private String sku;
    private Integer stockQty;
    private Float price;
    private String color;
    private String size;
    private boolean isActive;

//    private Float itemWeight;
//    private String genericName;
//    private String countryOfOrigin;
//    private String manufacturer;
//    private String packer;

    public ProductItemDetailsDto() {}

    public ProductItemDetailsDto(Long productItemId, String productName, String categoryName, String subCategoryName, String productDescription,
                                 boolean podAvailable, Date addedDate, String sku, Integer stockQty, Float price,
                                 String color, String size, boolean isActive) {
        this.productItemId = productItemId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.productDescription = productDescription;
        this.podAvailable = podAvailable;
        this.addedDate = addedDate;
        this.sku = sku;
        this.stockQty = stockQty;
        this.price = price;
        this.color = color;
        this.size = size;
        this.isActive = isActive;
    }

    public Long getProductItemId() {
        return productItemId;
    }

    public void setProductItemId(Long productItemId) {
        this.productItemId = productItemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public boolean isPodAvailable() {
        return podAvailable;
    }

    public void setPodAvailable(boolean podAvailable) {
        this.podAvailable = podAvailable;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String toString() {
        return "ProducctItemDetailsDto:\nproductItemId: " + productItemId + "\nproductName: " + productName +
                "\ncategoryName: " + categoryName + "\nsubCategoryName: " + subCategoryName + "\nproductDescription: " +
                productDescription + "\npodAvailable: " + podAvailable + "\naddedDate: " + addedDate + "\nsku: " + sku +
                "\nstockQty: " + stockQty + "\nprice: " + price + "\ncolor: " + color + "\nsize: " + size + "\nisActive: " +
                isActive + "\n";
    }
}
