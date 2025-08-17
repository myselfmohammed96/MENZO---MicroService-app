package com.menzo.Product_Service.Dto.ProductDto;

import com.menzo.Product_Service.Enum.ProductActiveStatus;

public class ProductItemListingDto {

    private Long id;
    private String sku;
    private Float price;
    private String size;
    private Integer stockQty;
    private ProductActiveStatus activeStatus;
    private String iconImage;

    public ProductItemListingDto() {}

    public ProductItemListingDto(Long id, String sku, Float price, String size, Integer stockQty,
                                 ProductActiveStatus activeStatus, String iconImage) {
        this.id = id;
        this.sku = sku;
        this.price = price;
        this.size = size;
        this.stockQty = stockQty;
        this.activeStatus = activeStatus;
        this.iconImage = iconImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public ProductActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ProductActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public void display() {
        System.out.println("ProductItemListingDto:\nid: " + id + "\nsku: " + sku + "\nprice: " +
                price + "\nsize: " + size + "\nstockQty: " + stockQty + "\nactiveStatus: " +
                activeStatus + "\niconImage: " + iconImage);
    }
}
