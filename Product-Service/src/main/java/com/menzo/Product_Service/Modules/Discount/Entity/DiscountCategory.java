package com.menzo.Product_Service.Modules.Discount.Entity;

import com.menzo.Product_Service.Modules.Category.Entity.ProductCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "discount_category")
public class DiscountCategory {

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
            name = "category_id",
            nullable = false
    )
    private ProductCategory category;

    @Column(
            name = "is_sub_category",
            nullable = false
    )
    private Boolean isSubCategory;

}
