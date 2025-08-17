package com.menzo.Product_Service.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "product_name")
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;   //  sub-category

    @Lob
    @Column(nullable = false, name = "product_description", columnDefinition = "MEDIUMTEXT")
    private String productDescription;

    //  ---------
    @Column(name = "generic_name")
    private String genericName;

    @Column(name = "item_weight")
    private Float itemWeight;

    @Column(name = "manufacturer_id")
    private Long manufacturerId;

    @Column(name = "packers_id")
    private Long packersId;

    @Column(name = "country_of_origin_id")
    private Long countryOfOriginId;

    //  ---------
    @Column(nullable = false, name = "pod_available")
    private Boolean podAvailable;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductItem> items = new ArrayList<>();

    @Column(nullable = false, name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private Date createdAt;

    public Product() {}

    public Product(String productName, ProductCategory category,
                   String productDescription, Boolean podAvailable) {
        this.productName = productName;
        this.category = category;
        this.productDescription = productDescription;
        this.podAvailable = podAvailable;
    }

    public Product(String productName, ProductCategory category,
                   String productDescription, Long countryOfOriginId, Boolean podAvailable) {
        this.productName = productName;
        this.category = category;
        this.productDescription = productDescription;
        this.countryOfOriginId = countryOfOriginId;
        this.podAvailable = podAvailable;
    }

    public Product(String productName, ProductCategory category, String productDescription,
                   Boolean podAvailable, Float itemWeight, String genericName, Long countryOfOriginId,
                   Long manufacturerId, Long packersId) {
        this.productName = productName;
        this.category = category;
        this.productDescription = productDescription;
        this.podAvailable = podAvailable;
        this.itemWeight = itemWeight;
        this.genericName = genericName;
        this. countryOfOriginId = countryOfOriginId;
        this.manufacturerId = manufacturerId;
        this.packersId = packersId;
    }

    public Product(Long id, String productName, ProductCategory category,
                   String productDescription, Boolean podAvailable, Date createdAt) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.productDescription = productDescription;
        this.podAvailable = podAvailable;
        this.createdAt = createdAt;
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

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public Float getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(Float itemWeight) {
        this.itemWeight = itemWeight;
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Long getPackersId() {
        return packersId;
    }

    public void setPackersId(Long packersId) {
        this.packersId = packersId;
    }

    public Long getCountryOfOriginId() {
        return countryOfOriginId;
    }

    public void setCountryOfOriginId(Long countryOfOriginId) {
        this.countryOfOriginId = countryOfOriginId;
    }

    public Boolean getPodAvailable() {
        return podAvailable;
    }

    public void setPodAvailable(Boolean podAvailable) {
        this.podAvailable = podAvailable;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String toString() {
        return "Product:\nid: " + id + "\nproductName: " + productName + "\ncategory: " +
                category + "\nproductDescription: " + productDescription + "\ngenericName: " + genericName +
                "\nitemWeight: " + itemWeight + "\nmanufacturerId: " + manufacturerId + "\npackersId: " + packersId +
                "\ncountryOfOriginId: " + countryOfOriginId + "\npodAvailable: " + podAvailable + "\nitems: " +
                items + "\ncreatedAt: " + createdAt + "\n";
    }
}

