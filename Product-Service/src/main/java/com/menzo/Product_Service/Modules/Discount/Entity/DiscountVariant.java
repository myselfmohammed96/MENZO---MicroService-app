package com.menzo.Product_Service.Modules.Discount.Entity;

import com.menzo.Product_Service.Modules.Product.Entity.ProductItem;
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
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "discount_id",
            nullable = false
    )
    private Discount discount;

    @ManyToOne
    @JoinColumn(
            name = "product_item_id",
            nullable = false
    )
    private ProductItem productItem;

}
