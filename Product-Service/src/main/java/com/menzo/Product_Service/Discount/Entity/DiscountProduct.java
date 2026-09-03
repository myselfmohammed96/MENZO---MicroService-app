package com.menzo.Product_Service.Discount.Entity;

import com.menzo.Product_Service.Product.Entity.Product;
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
        name = "discount_products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_discount_product",
                        columnNames = { "discount_id", "product_id" }
                )
        }
)
public class DiscountProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID discountProductId;

    @ManyToOne
    @JoinColumn(
            name = "discount_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_discount_product_discount")
    )
    private Discount discount;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_discount_product_product")
    )
    private Product product;

}
