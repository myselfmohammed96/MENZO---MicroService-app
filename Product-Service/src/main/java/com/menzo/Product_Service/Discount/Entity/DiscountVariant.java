package com.menzo.Product_Service.Discount.Entity;

import com.menzo.Product_Service.Product.Entity.ProductItem;
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
        name = "discount_variants",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_discount_variant",
                        columnNames = { "discount_id", "product_item_id" }
                )
        }
)
public class DiscountVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID discountVariantId;

    @ManyToOne
    @JoinColumn(
            name = "discount_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_discount_variant_discount")
    )
    private Discount discount;

    @ManyToOne
    @JoinColumn(
            name = "product_item_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_discount_variant_variant")
    )
    private ProductItem productItem;

}
