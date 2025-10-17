package com.menzo.Product_Service.Dto.ProductDto;

import java.util.Map;

public class NewProductDto {

    private String productName;
    private String description;
    private Map<Long, Integer> sizeStockMap;
    private Long color;
    private String status;
    private String pod;

    private Float price;
    //    discount
    //    discountType

    private Float itemWeight;
    private String genericName;
    private String countryOfOrigin;
    private String manufacturer;
    private String packer;

    private Long categoryId;
    private Long subCategoryId;

    //    variations
    //////////////////////////////////////////

    public NewProductDto() {}

    public NewProductDto(String status, String pod) {
        this.status = status;
        this.pod = pod;
    }

    public NewProductDto(String productName, Long categoryId, Long subCategoryId,
                         String description, String pod, Float itemWeight, String genericName,
                         String countryOfOrigin, String manufacturer, String packer) {
        this.productName = productName;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.description = description;
        this.pod = pod;
        this.itemWeight = itemWeight;
        this.genericName = genericName;
        this.countryOfOrigin = countryOfOrigin;
        this.manufacturer = manufacturer;
        this.packer = packer;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Long, Integer> getSizeStockMap() {
        return sizeStockMap;
    }

    public void setSizeStockMap(Map<Long, Integer> sizeStockMap) {
        this.sizeStockMap = sizeStockMap;
    }

    public Long getColor() {
        return color;
    }

    public void setColor(Long color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String toString() {
        return "NewProductDto:\nproductName: " + productName + "\ndescription: " + description +
                "\ncolor: " + color + "\nstatus: " + status + "\npod: " + pod + "\nprice: " + price +
                "\nitemWeight: " + itemWeight + "\ngenericName: " + genericName + "\ncountryOfOrigin: " +
                countryOfOrigin + "\nmanufacturer: " + manufacturer + "\npacker: " + packer +
                "\ncategoryId: " + categoryId + "\nsubCategoryId: " + subCategoryId;
    }
}











//    private String productName;
//    private Long categoryId;
//    private Long subCategoryId;
//    private String description;
//    private String cod;
//    private Float itemWeight;
//    private String genericName;
//    private String countryOfOrigin;
//    private String manufacturer;
//    private String packer;
