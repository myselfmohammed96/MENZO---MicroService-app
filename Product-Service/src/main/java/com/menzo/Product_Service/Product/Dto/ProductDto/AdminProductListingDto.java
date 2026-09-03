package com.menzo.Product_Service.Product.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminProductListingDto {

    private UUID productId;

    private String productName;

    private String productCode;

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

    private String iconImage;

}
