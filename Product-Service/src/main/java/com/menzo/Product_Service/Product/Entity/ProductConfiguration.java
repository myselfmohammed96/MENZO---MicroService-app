package com.menzo.Product_Service.Product.Entity;

import com.menzo.Product_Service.Variation.Entity.VariationOption;
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
        name = "product_configurations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_option_item_product_config",
                columnNames = {
                        "product_item_id",
                        "variation_option_id"
                }
        )
)
public class ProductConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID productConfigurationId;

    @ManyToOne
    @JoinColumn(
            name = "product_item_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_item_config")
    )
    private ProductItem productItem;

    @ManyToOne
    @JoinColumn(
            name = "variation_option_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_variation_option_config")
    )
    private VariationOption variationOption;

}
