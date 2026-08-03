package com.menzo.Product_Service.Product.Entity;

import com.menzo.Product_Service.Variation.Entity.VariationOption;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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
    private Long productConfigurationId;

    @ManyToOne
    @JoinColumn(name = "product_item_id", nullable = false)
    private ProductItem productItem;

    @ManyToOne
    @JoinColumn(name = "variation_option_id", nullable = false)
    private VariationOption variationOption;

}
