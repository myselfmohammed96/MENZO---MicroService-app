package com.menzo.Product_Service.Modules.Discount.Entity;

import com.menzo.Product_Service.Modules.Product.Entity.Product;
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
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "discount_id",
            nullable = false
    )
    private Discount discount;

    @ManyToOne
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;

}
