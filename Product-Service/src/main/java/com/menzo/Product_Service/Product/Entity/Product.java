package com.menzo.Product_Service.Product.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Product.Dto.AdminProductListingDto;
import com.menzo.Product_Service.Product.Dto.UserProductListingDto;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@ToString(exclude = {
//        "items",
//        "category"
//})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "AdminProductListingDtoMapping",
                classes = @ConstructorResult(
                        targetClass = AdminProductListingDto.class,
                        columns = {
                                @ColumnResult(name = "productId", type = Long.class),
                                @ColumnResult(name = "productName", type = String.class),
                                @ColumnResult(name = "subCategoryName", type = String.class),
                                @ColumnResult(name = "categoryName", type = String.class),
                                @ColumnResult(name = "minPrice", type = Float.class),
                                @ColumnResult(name = "maxPrice", type = Float.class),
                                @ColumnResult(name = "minStockQty", type = Integer.class),
                                @ColumnResult(name = "maxStockQty", type = Integer.class),
                                @ColumnResult(name = "latestCreatedAt", type = Timestamp.class),
                                @ColumnResult(name = "oldestCreatedAt", type = Timestamp.class),
                                @ColumnResult(name = "colorCount", type = Integer.class),
                                @ColumnResult(name = "activeStatus", type = String.class),
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "UserProductListingDtoMapping",
                classes = @ConstructorResult(
                        targetClass = UserProductListingDto.class,
                        columns = {
                                @ColumnResult(name = "productId", type = Long.class),
                                @ColumnResult(name = "productName", type = String.class),
                                @ColumnResult(name = "superSku", type = String.class),
                                @ColumnResult(name = "minPrice", type = Float.class),
                                @ColumnResult(name = "maxPrice", type = Float.class),
                                @ColumnResult(name = "minStockQty", type = Integer.class),
                                @ColumnResult(name = "iconImage", type = String.class)
                        }
                )
        )
})
@Table(name = "products")
@FilterDef(
        name = "productFilter",
        parameters = {
                @ParamDef(name = "isActive", type = Boolean.class),
                @ParamDef(name = "isDeleted", type = Boolean.class)
        }
)
@Filter(
        name = "productFilter",
        condition = "is_active = :isActive AND is_deleted = :isDeleted"
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, unique = true)
    private String productCode;

    //  uni-directional mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory subCategory;   //  sub-category - ## name change to sub-category

    @Lob
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String productDescription;

    @Column(nullable = false)
    private boolean podAvailable;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private boolean isDeleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //  ---------
    private String genericName;

    private Float itemWeight;

    private Long manufacturerId;

    private Long packersId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "county_of_origin_id", nullable = false)
    private CountryOfOrigin countryOfOrigin;

    //  ---------

    @JsonIgnore
    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<ProductItem> items = new ArrayList<>();

}
