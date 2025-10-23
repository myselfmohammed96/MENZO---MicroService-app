package com.menzo.Product_Service.Dto.ProductDto;

import com.menzo.Product_Service.Enum.ProductActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemListingDto {

    private Long id;

    private String sku;

    private Float price;

    private String size;

    private Integer stockQty;

    private ProductActiveStatus activeStatus;

    private String iconImage;

}
