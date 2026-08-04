package com.menzo.Product_Service.Product.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "product_images",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"super_sku", "image_url"}),
                @UniqueConstraint(columnNames = {"super_sku", "image_order"})
        }
)
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productImageId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String superSku;

    @Column(nullable = false)
    private int imageOrder;

    @Column(nullable = false)
    private boolean isPrimaryImage;

    @ManyToMany
    @JoinTable(
            name = "item_image_configuration",
            joinColumns = @JoinColumn(name = "image_id", referencedColumnName = "product_image_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    )
    private List<ProductItem> productItems = new ArrayList<>();

}