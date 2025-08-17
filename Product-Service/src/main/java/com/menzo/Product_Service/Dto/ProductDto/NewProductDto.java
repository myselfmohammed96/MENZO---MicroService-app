package com.menzo.Product_Service.Dto.ProductDto;

public class NewProductDto {

    private String productName;
    private Long categoryId;
    private Long subCategoryId;
    private String description;
    private String cod;
    private Float itemWeight;
    private String genericName;
    private String countryOfOrigin;
    private String manufacturer;
    private String packer;

    public NewProductDto() {}

    public NewProductDto(String productName, Long categoryId, Long subCategoryId,
                         String description, String cod, Float itemWeight, String genericName,
                         String countryOfOrigin, String manufacturer, String packer) {
        this.productName = productName;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.description = description;
        this.cod = cod;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
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

    public void display() {
        System.out.println("NewProductDto:\nproductName: " + productName + "\ncategoryId: " + categoryId +
                "\nsubCategoryId: " + subCategoryId + "\ndescription: " + description + "\ncod: " + cod +
                "\nitemWeight: " + itemWeight + "\ngenericName: " + genericName + "\ncountryOfOrigin: " +
                countryOfOrigin + "\nmanufacturer: " + manufacturer + "\npacker: " + packer);
    }
}
