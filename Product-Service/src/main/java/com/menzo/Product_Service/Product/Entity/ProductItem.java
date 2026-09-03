package com.menzo.Product_Service.Product.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@ToString(exclude = {"configurations", "images"})
@Table(
        name = "product_items",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_product_item_sku",
                columnNames = "sku"
        )
)
@FilterDef(
        name = "itemFilter",
        parameters = {
                @ParamDef(name = "isActive", type = Boolean.class),
                @ParamDef(name = "isDeleted", type = Boolean.class)
        }
)
@Filter(
        name = "itemFilter",
        condition = "is_active = :isActive AND is_deleted = :isDeleted"
)
public class ProductItem {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
//    @SequenceGenerator(
//            name = "item_sequence",
//            sequenceName = "item_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "item_sequence"
//    )
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id")
    private UUID itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_product")
    )
    private Product product;

    @Column(nullable = false)
    private String superSku;

    @Column(name = "sku", nullable = false)
    private String SKU;

    @Column(nullable = false)
    private Integer qtyInStock;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private BigDecimal mrp;

    private BigDecimal minSellingPrice;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "productItems", cascade = CascadeType.ALL)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "productItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductConfiguration> configurations = new ArrayList<>();

}
