package com.menzo.Product_Service.Discount.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.Product_Service.Discount.Enum.CapType;
import com.menzo.Product_Service.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Discount.Enum.DiscountType;
import com.menzo.Product_Service.Discount.Enum.OperationalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "discount",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_discount_code",
                columnNames = "discount_code"
        )
)
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID discountId;

    @Column(nullable = false, unique = true)
    private String discountCode;    //  ## unique index (indexing) and other indexes

    @Column(nullable = false)
    private String discountName;

    private String discountDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountLevel discountLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;       //  ## precision/scale for price fields @Column(precision = 10, scale = 2)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CapType capType;

    private BigDecimal capValue;

    private Integer priority;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime resumeAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationalStatus discountStatus;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "discount",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<DiscountCategory> discountCategories = new HashSet<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "discount",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<DiscountProduct> discountProducts = new HashSet<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "discount",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<DiscountVariant> discountVariants = new HashSet<>();

//    @PreUpdate
//    void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "discount_products",
//            joinColumns = @JoinColumn(
//                    name = "discount_id",
//                    referencedColumnName = "id"
//            ),
//            inverseJoinColumns = @JoinColumn(
//                    name = "product_id",
//                    referencedColumnName = "id"
//            )
//    )

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "discount_variants",
//            joinColumns = @JoinColumn(
//                    name = "discount_id",
//                    referencedColumnName = "id"
//            ),
//            inverseJoinColumns = @JoinColumn(
//                    name = "product_item_id",
//                    referencedColumnName = "id"
//            )
//    )

}
