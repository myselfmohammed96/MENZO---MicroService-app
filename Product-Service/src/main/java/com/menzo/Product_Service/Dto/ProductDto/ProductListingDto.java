package com.menzo.Product_Service.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListingDto {

    private Long productId;

    private String productName;

    private String subCategoryName;

    private String categoryName;

    private Float minPrice;

    private Float maxPrice;

    private Integer minStockQty;

    private Integer maxStockQty;

    private Date latestCreatedAt;

    private Date oldestCreatedAt;

    private Integer colorCount;

    private String activeStatus;


//    private Long id;
//
//    private String productName;
//
//    private String subCategoryName;
//
//    private Float basePrice;
//
//    private Integer totalItems;
//
//    private ProductActiveStatus activeStatus;
//
//    private String iconImage;

}
