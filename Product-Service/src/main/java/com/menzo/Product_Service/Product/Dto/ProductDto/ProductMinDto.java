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
public class ProductMinDto {

    private UUID productId;

    private String productName;

    private String iconImage;

}
