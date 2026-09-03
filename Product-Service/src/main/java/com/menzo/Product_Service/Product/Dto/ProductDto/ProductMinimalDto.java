package com.menzo.Product_Service.Product.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMinimalDto {

    private UUID productId;

    private String productName;

    private UUID categoryId;

    private String categoryName;

    private UUID subCategoryId;

    private String subCategoryName;

}
