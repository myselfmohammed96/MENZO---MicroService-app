package com.menzo.Product_Service.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProductListingDto {

    private Long productId;

    private String productName;

    private String superSku;

    private Float minPrice;

    private Float maxPrice;

    private Integer minStockQty;

    //  discount details

    private String iconImage;

//    private List<String> colors;

    //  'limited time deal' details

    //  free delivery details

}
