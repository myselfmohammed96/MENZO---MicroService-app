package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_items")
public class ProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true, name = "SKU")
    private String SKU;

    @Column(nullable = false, name = "qty_in_stock")
    private Integer qtyInStock;

    @Column(nullable = false, name = "price")
    private Float price;

    @OneToMany(mappedBy = "productItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductConfiguration> configurations = new ArrayList<>();

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;

    public ProductItem() {}

    public ProductItem(Product product, String SKU, Integer qtyInStock,
                       Float price, Boolean isActive) {
        this.product = product;
        this.SKU = SKU;
        this.qtyInStock = qtyInStock;
        this.price = price;
        this.isActive = isActive;
    }

    public ProductItem(Product product, String SKU, Integer qtyInStock,
                       Float price, Boolean isActive, List<ProductConfiguration> configurations) {
        this.product = product;
        this.SKU = SKU;
        this.qtyInStock = qtyInStock;
        this.price = price;
        this.isActive = isActive;
        this.configurations = configurations;
    }

    public ProductItem(Long id, Product product, String SKU,
                       Integer qtyInStock, Float price, Boolean isActive) {
        this.id = id;
        this.product = product;
        this.SKU = SKU;
        this.qtyInStock = qtyInStock;
        this.price = price;
        this.isActive = isActive;
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

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public Integer getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(Integer qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public List<ProductConfiguration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<ProductConfiguration> configurations) {
        this.configurations = configurations;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void display() {
        System.out.println("ProductItem:\nid: " + id + "\nproduct: " + product + "\nSKU: " + SKU +
                "\nqtyInStock: " + qtyInStock + "\nprice: " + price + "\nconfigurations: " + configurations +
                "\nisActive: " + isActive);
    }
}
