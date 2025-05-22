package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "product_categories")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Self-referencing relationship
//    @ManyToOne
//    @JoinColumn(name = "parent_category_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "product_categories_ibfk_1"))
    @Column(name = "parent_category_id")
    private Long parentCategoryId;

    @Column(name = "category_name", nullable = false, unique = true, length = 100)
    private String categoryName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public ProductCategory(){}

    public ProductCategory(Long parentCategoryId,
                           String categoryName){
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
    }

    public ProductCategory(String categoryName){
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = this.parentCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
