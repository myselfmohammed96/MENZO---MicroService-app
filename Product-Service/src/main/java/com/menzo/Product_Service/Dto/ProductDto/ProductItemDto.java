package com.menzo.Product_Service.Dto.ProductDto;

import com.menzo.Product_Service.Entity.Product;

public class ProductItemDto {

    private Long productId;
    private Product product;
    private Long colorId;
    private Float price;
    private boolean isActive;

    public ProductItemDto() {}

    public ProductItemDto(Long productId, Product product, Long colorId,
                          Float price, boolean isActive) {
        this.productId = productId;
        this.product = product;
        this.colorId = colorId;
        this.price = price;
        this.isActive = isActive;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
