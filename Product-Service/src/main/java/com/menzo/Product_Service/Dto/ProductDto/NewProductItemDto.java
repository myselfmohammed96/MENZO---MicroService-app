package com.menzo.Product_Service.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductItemDto {

    private Long productId;

    private String sku;

    private Float price;

    private Integer stockQty;

    private String status;

    private Map<String, String> variations;

}
