package com.menzo.Product_Service.Modules.Product.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;

    @Column(
            name = "super_sku",
            nullable = false
    )
    private String superSku;

//    @ManyToOne
//    @JoinColumn(name = "product_item_id")
//    private ProductItem productItem;

    @ManyToMany
    @JoinTable(
            name = "item_image_configuration",
            joinColumns = @JoinColumn(
                    name = "image_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "item_id",
                    referencedColumnName = "id"
            )
    )
    private List<ProductItem> productItems = new ArrayList<>();

    @Column(
            name = "image_url",
            nullable = false
    )
    private String imageUrl;

}
