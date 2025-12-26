package com.menzo.Product_Service.Modules.Discount.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.Product_Service.Modules.Discount.Enum.CapType;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountType;
import com.menzo.Product_Service.Modules.Discount.Enum.PromotionStatus;
import com.menzo.Product_Service.Modules.Product.Entity.Product;
import com.menzo.Product_Service.Modules.Product.Entity.ProductItem;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "discount_code",
            nullable = false,
            unique = true
    )
    private String discountCode;    //  ## unique index (indexing) and other indexes

    @Column(
            name = "discount_name",
            nullable = false
    )
    private String discountName;

    @Column(name = "discount_description")
    private String discountDescription;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "discount_level",
            nullable = false
    )
    private DiscountLevel level;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "discount_type",
            nullable = false
    )
    private DiscountType type;

    @Column(
            name = "discount_value",
            nullable = false
    )
    private BigDecimal value;       //  ## precision/scale for price fields @Column(precision = 10, scale = 2)

    @Enumerated(EnumType.STRING)
    @Column(
            name = "cap_type",
            nullable = false
    )
    private CapType capType;

    @Column(name = "cap_value")
    private BigDecimal capValue;

    @Column(name = "priority")
    private Integer priority;

    @Column(
            name = "start_at",
            nullable = false
    )
    private LocalDateTime startAt;

    @Column(
            name = "end_at",
            nullable = false
    )
    private LocalDateTime endAt;

    @Nullable
    @Column(name = "resume_at")
    private LocalDateTime resumeAt;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "discount_status",
            nullable = false
    )
    private PromotionStatus discountStatus;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "discount",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<DiscountCategory> discountCategories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "discount_products",
            joinColumns = @JoinColumn(
                    name = "discount_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Product> discountProducts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "discount_variants",
            joinColumns = @JoinColumn(
                    name = "discount_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_item_id",
                    referencedColumnName = "id"
            )
    )
    private Set<ProductItem> discountVariants = new HashSet<>();

    @Column(
            name = "is_deleted",
            nullable = false
    )
    private Boolean isDeleted;

    @CreationTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime updatedAt;

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
