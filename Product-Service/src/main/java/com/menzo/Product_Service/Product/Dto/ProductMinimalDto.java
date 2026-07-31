package com.menzo.Product_Service.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMinimalDto {

    private Long productId;

    private String productName;

    private Long categoryId;

    private String categoryName;

    private Long subCategoryId;

    private String subCategoryName;

}
