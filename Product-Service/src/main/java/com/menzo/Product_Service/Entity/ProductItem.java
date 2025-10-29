package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "configurations")
@Table(name = "product_items")
public class ProductItem {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @SequenceGenerator(
            name = "item_sequence",
            sequenceName = "item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "item_sequence"
    )
    private Long id;





    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Product product;




    @ManyToMany(
            mappedBy = "productItems",
            cascade = CascadeType.ALL
    )
    private List<ProductImage> images = new ArrayList<>();

    @Column(
            nullable = false,
            name = "super_sku"
    )
    private String superSKU;

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
