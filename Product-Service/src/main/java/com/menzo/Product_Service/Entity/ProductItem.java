package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_items")
public class ProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;

    @Column(
            nullable = false,
            unique = true,
            name = "SKU"
    )
    private String SKU;

    @Column(
            nullable = false,
            name = "qty_in_stock"
    )
    private Integer qtyInStock;

    @Column(
            nullable = false,
            name = "price"
    )
    private Float price;

    @OneToMany(
            mappedBy = "productItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductConfiguration> configurations = new ArrayList<>();

    @Column(
            nullable = false,
            name = "is_active"
    )
    private Boolean isActive;

}
