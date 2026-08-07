package com.menzo.Product_Service.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductMinDto {

    private Long productId;

    private String productName;

    private String iconImage;

}
