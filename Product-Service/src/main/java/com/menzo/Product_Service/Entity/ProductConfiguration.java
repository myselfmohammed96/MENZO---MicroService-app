package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_configurations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_item_id", "variation_option_id"})
})
public class ProductConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_item_id", nullable = false)
    private ProductItem productItem;

    @ManyToOne
    @JoinColumn(name = "variation_option_id", nullable = false)
    private VariationOption variationOption;

    public ProductConfiguration() {}

    public ProductConfiguration(ProductItem productItem, VariationOption variationOption) {
        this.productItem = productItem;
        this.variationOption = variationOption;
    }

    public ProductConfiguration(Long id, ProductItem productItem, VariationOption variationOption) {
        this.id = id;
        this.productItem = productItem;
        this.variationOption = variationOption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public VariationOption getVariationOption() {
        return variationOption;
    }

    public void setVariationOption(VariationOption variationOption) {
        this.variationOption = variationOption;
    }

    public void display() {
        System.out.println("ProductConfiguration:\nid: " + id + "\nproductItem: " + productItem +
                "\nvariationOption: " + variationOption);
    }
}
