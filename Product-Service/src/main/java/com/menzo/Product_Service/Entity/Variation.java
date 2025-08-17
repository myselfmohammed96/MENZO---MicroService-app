package com.menzo.Product_Service.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "variations")
public class Variation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "variation_name")
    private String variationName;

    @JsonIgnore
    @ManyToMany(mappedBy = "variations")
    private Set<ProductCategory> categories = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id")
    private Set<VariationOption> options = new HashSet<>();

    @Column(nullable = false, name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @CreationTimestamp
    private Date createdAt;

    public Variation(){}

    public Variation(String variationName){
        this.variationName = variationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariationName() {
        return variationName;
    }

    public void setVariationName(String variationName) {
        this.variationName = variationName;
    }

    public Set<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<ProductCategory> categories) {
        this.categories = categories;
    }

    public Set<VariationOption> getOptions() {
        return options;
    }

    public void setOptions(Set<VariationOption> options) {
        this.options = options;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void display() {
        System.out.println("Variation:\nid: " + id + "\nvariationName: " + variationName +
                "\ncategories: " + categories + "\noptions: " + options + "\ncreatedAt: " + createdAt);
    }
}
