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
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productImageId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String superSku;

    @ManyToMany
    @JoinTable(
            name = "item_image_configuration",
            joinColumns = @JoinColumn(name = "image_id", referencedColumnName = "product_image_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    )
    private List<ProductItem> productItems = new ArrayList<>();

    //    @ManyToOne
    //    @JoinColumn(name = "product_id")
    //    private Product product;

    //    @ManyToOne
    //    @JoinColumn(name = "product_item_id")
    //    private ProductItem productItem;
}
