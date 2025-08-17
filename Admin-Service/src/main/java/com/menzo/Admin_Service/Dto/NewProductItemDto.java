package com.menzo.Admin_Service.Dto;

public class NewProductItemDto {

    private Long productId;
    private String sku;
    private Float price;
    private Integer stockQty;
    private String status;

    public NewProductItemDto() {}

    public NewProductItemDto(Long productId) {
        this.productId = productId;
    }

    public NewProductItemDto(Long productId, String sku, Float price,
                             Integer stockQty, String status) {
        this.productId = productId;
        this.sku = sku;
        this.price = price;
        this.stockQty = stockQty;
        this.status = status;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void display() {
        System.out.println("NewProductItemDto:\nproductId: " + productId + "\nsku: " + sku +
                "\nprice: " + price + "\nstockQty: " + stockQty + "\nstatus: " + status);
    }
}
