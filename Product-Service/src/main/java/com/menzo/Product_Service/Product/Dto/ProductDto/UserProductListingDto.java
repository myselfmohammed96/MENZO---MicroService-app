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
public class UserProductListingDto {

    private UUID productId;

    private String productName;

    private String productCode;

    private String superSku;

    private Float minPrice;

    private Float maxPrice;

    private Integer minStockQty;

    private String iconImage;

//    private List<String> colors;

    //  discount details

    //  'limited time deal' details

    //  free delivery details

}
