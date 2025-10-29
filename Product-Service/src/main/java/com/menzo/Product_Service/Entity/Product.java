package com.menzo.Product_Service.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {
        "items",
        "category"
})
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            name = "product_name"
    )
    private String productName;

    //  uni-directional mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private ProductCategory category;   //  sub-category - ## name change to sub-category

    @Lob
    @Column(
            nullable = false,
            name = "product_description",
            columnDefinition = "MEDIUMTEXT"
    )
    private String productDescription;

    //  ---------
    @Column(name = "generic_name")
    private String genericName;

    @Column(name = "item_weight")
    private Float itemWeight;

    @Column(name = "manufacturer_id")
    private Long manufacturerId;

    @Column(name = "packers_id")
    private Long packersId;

    @Column(name = "country_of_origin_id")
    private Long countryOfOriginId;

    //  ---------
    @Column(
            nullable = false,
            name = "pod_available"
    )
    private Boolean podAvailable;





    @JsonIgnore
    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<ProductItem> items = new ArrayList<>();





    @Column(
            nullable = false,
            name = "created_at"
    )
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy"
    )
    @CreationTimestamp
    private Date createdAt;

}

