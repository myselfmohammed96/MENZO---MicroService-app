package com.menzo.Product_Service.Product.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long productId;

    private String productName;

    private String productCode;

    private Long categoryId;

    private Long subCategoryId;

    private String productDescription;

    private String genericName;

    private float itemWeight;

    private Long manufacturerId;

    private Long packerId;

    private String countyOfOrigin;

}

