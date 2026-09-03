package com.menzo.Product_Service.Product.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private UUID productId;

    private String productName;

    private String productCode;

    private UUID categoryId;

    private UUID subCategoryId;

    private String productDescription;

    private String genericName;

    private BigDecimal itemWeight;

    private UUID manufacturerId;

    private UUID packerId;

    private String countyOfOrigin;

}

