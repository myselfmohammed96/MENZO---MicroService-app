package com.menzo.Product_Service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_category_id")
    private Long parentCategoryId;

    @Column(name = "category_name", nullable = false, unique = true, length = 100)
    private String categoryName;

//    @JsonManagedReference("category-variation")
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "category_variation_configuration",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns =  @JoinColumn(name = "variation_id")
    )
    private Set<Variation> variations = new HashSet<>();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public ProductCategory(){}

    public ProductCategory(String categoryName){
        this.categoryName = categoryName;
    }

    public ProductCategory(Long parentCategoryId, String categoryName){
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
    }

    public ProductCategory(Long parentCategoryId, String categoryName, Set<Variation> variations) {
        this.parentCategoryId = parentCategoryId;
        this.categoryName = categoryName;
        this.variations = variations;
    }

    public ProductCategory(SubCategoryDto subCategoryDto) {
        this.id = subCategoryDto.getId();
        this.parentCategoryId = subCategoryDto.getParentCategoryId();
        this.categoryName = subCategoryDto.getCategoryName();
        this.isActive = subCategoryDto.getIsActive();
        this.createdAt = subCategoryDto.getCreatedAt();
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
        this.parentCategoryId = parentCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Variation> getVariations() {
        return variations;
    }

    public void setVariations(Set<Variation> variations) {
        this.variations = variations;
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

    public void display() {
        System.out.println("id: " + id + "\nparentCategoryId: " + parentCategoryId +
                "\ncategoryName: " + categoryName + "\nvariations: " + variations +
                "\nisActive: " + isActive + "\ncreatedAt: " + createdAt);
    }
}
