package com.menzo.Product_Service.Discount.Entity;

import com.menzo.Product_Service.Category.Entity.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "discount_category",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_discount_category",
                        columnNames = { "discount_id", "category_id" }
                )
        }
)
public class DiscountCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID discountCategoryId;

    @ManyToOne
    @JoinColumn(
            name = "discount_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_discount_category_discount")
    )
    private Discount discount;

    @ManyToOne
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_discount_category_category")
    )
    private ProductCategory category;

    @Column(nullable = false)
    private boolean isSubCategory;

}
