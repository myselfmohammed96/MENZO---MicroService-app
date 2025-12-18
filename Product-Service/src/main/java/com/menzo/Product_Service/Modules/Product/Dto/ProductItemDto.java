package com.menzo.Product_Service.Modules.Product.Dto;

import com.menzo.Product_Service.Modules.Product.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemDto {

    private Long productId;

    private Product product;

    private Long colorId;

    private Float price;

    private boolean isActive;

}
