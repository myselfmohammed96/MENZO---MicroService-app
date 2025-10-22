package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Column(name = "super_sku", nullable = false)
    private String superSku;

    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private ProductItem productItem;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    public ProductImage() {}

    public ProductImage(String superSku, ProductItem productItem, String imageUrl) {
        this.superSku = superSku;
        this.productItem = productItem;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }


    public String getSuperSku() {
        return superSku;
    }

    public void setSuperSku(String superSku) {
        this.superSku = superSku;
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
        System.out.println("ProductImage:\nid: " + id + "\nproductItem: " +
                productItem + "\nimageUrl: " + imageUrl);
    }
}
