package com.menzo.Admin_Service.Dto;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsDto {

    private Long productId;
    private String productName;
    private String categoryName;
    private String subCategoryName;
    private String productDescription;
    private boolean podAvailable;

    private Float itemWeight;
    private String genericName;
    private String countryOfOrigin;
    private String manufacturer;
    private String packer;
    private List<String> imageUrls = new ArrayList<>();

    public ProductDetailsDto() {}

    public ProductDetailsDto(Long productId, String productName, String categoryName,
                             String subCategoryName, String productDescription, boolean podAvailable,
                             Float itemWeight, String genericName, String countryOfOrigin,
                             String manufacturer, String packer, List<String> imageUrls) {
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.productDescription = productDescription;
        this.podAvailable = podAvailable;
        this.itemWeight = itemWeight;
        this.genericName = genericName;
        this.countryOfOrigin = countryOfOrigin;
        this.manufacturer = manufacturer;
        this.packer = packer;
        this.imageUrls = imageUrls;
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

    public Float getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(Float itemWeight) {
        this.itemWeight = itemWeight;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPacker() {
        return packer;
    }

    public void setPacker(String packer) {
        this.packer = packer;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void display() {
        System.out.println("ProductDetailsDto:\nproductId: " + productId + "\nproductName: " + productName +
                "\ncategoryName: " + categoryName + "\nsubCategoryName: " + subCategoryName + "\nproductDescription: " +
                productDescription + "\npodAvailable: " + podAvailable + "\nitemWeight: " + itemWeight + "\ngenericName: " +
                genericName + "\ncountryOfOrigin: " + countryOfOrigin + "\nmanufacturer: " + manufacturer + "\npacker: " + packer);
    }
}
