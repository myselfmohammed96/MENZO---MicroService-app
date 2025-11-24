package com.menzo.Product_Service.Dto.ProductDto;

import com.menzo.Product_Service.Enum.ProductActiveStatus;
import com.menzo.Product_Service.Enum.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemListingDto {

//    private Long id;

    private String superSku;

    private StockStatus stockStatus;

    private ProductActiveStatus activeStatus;

    private String color;

    private String hexCode;

    private String iconImage;

//    private ItemDetailsDto itemDetails;

}
