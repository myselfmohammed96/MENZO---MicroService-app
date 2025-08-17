package com.menzo.Admin_Service.Dto;

import com.menzo.Admin_Service.Enum.ActiveStatus;

public class ProductListingDto {

    private Long id;
    private String productName;
    private String subCategoryName;
    private Float startingPrice;
    private Integer totalItems;
    private ActiveStatus activeStatus;
    private String iconImage;

    public ProductListingDto() {}

    public ProductListingDto(Long id, String productName, String subCategoryName, Float startingPrice,
                             Integer totalItems, ActiveStatus activeStatus, String iconImage) {
        this.id = id;
        this.productName = productName;
        this.subCategoryName = subCategoryName;
        this.startingPrice = startingPrice;
        this.totalItems = totalItems;
        this.activeStatus = activeStatus;
        this.iconImage = iconImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public Float getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Float startingPrice) {
        this.startingPrice = startingPrice;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public void display() {
        System.out.println("ProductListingDto:\nid: " + id + "\nproductName: " + productName + "\nsubCategoryName: " +
                subCategoryName + "\nstartingPrice: " + startingPrice + "\ntotalItems: " + totalItems + "\nactiveStatus: " +
                activeStatus + "\niconImage: " + iconImage);
    }
}
