package com.menzo.Product_Service.Modules.Discount.Entity;

import com.menzo.Product_Service.Modules.Category.Entity.ProductCategory;
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
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "discount_id",
            nullable = false
    )
    private Discount discount;

    @ManyToOne
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private ProductCategory category;

    @Column(
            name = "is_sub_category",
            nullable = false
    )
    private Boolean isSubCategory;

}
