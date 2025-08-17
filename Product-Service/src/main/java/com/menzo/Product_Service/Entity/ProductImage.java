package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private ProductItem productItem;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    public ProductImage() {}

    public ProductImage(Product product, ProductItem productItem, String imageUrl) {
        this.product = product;
        this.productItem = productItem;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void display() {
        System.out.println("ProductImage:\nid: " + id + "\nproduct: " + product +
                "\nproductItem: " + productItem + "\nimageUrl: " + imageUrl);
    }
}
