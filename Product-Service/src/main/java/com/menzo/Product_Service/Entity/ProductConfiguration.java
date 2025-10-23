package com.menzo.Product_Service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "product_configurations",
        uniqueConstraints = { @UniqueConstraint(
                        columnNames = {
                                "product_item_id",
                                "variation_option_id"
                        })
})
public class ProductConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "product_item_id",
            nullable = false
    )
    private ProductItem productItem;

    @ManyToOne
    @JoinColumn(
            name = "variation_option_id",
            nullable = false
    )
    private VariationOption variationOption;

}
