package com.menzo.Product_Service.Modules.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMinimalListingDto {

    private Long productId;
    private String productName;
    private Long itemId;
    private String itemName;
}
