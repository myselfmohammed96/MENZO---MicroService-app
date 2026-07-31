package com.menzo.Product_Service.Product.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = { "configurations", "images" })
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
    private String superSku;

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
            name = "mrp"
    )
    private BigDecimal mrp;

    @Column(
            nullable = false,
            name = "selling_price"
    )
    private BigDecimal sellingPrice;

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

    @Column(
            nullable = false,
            name = "created_at"
    )
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy"
    )
    @CreationTimestamp
    private Date createdAt;

}
