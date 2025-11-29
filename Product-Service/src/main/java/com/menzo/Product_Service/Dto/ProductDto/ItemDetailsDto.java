package com.menzo.Product_Service.Dto.ProductDto;

import com.menzo.Product_Service.Enum.ProductActiveStatus;
import com.menzo.Product_Service.Enum.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetailsDto {


    private Float startingPrice;

    private List<String> imageUrls;

    private List<ItemSizeDto> sizeDetails;


    //  optional

    private String superSku;

    private StockStatus stockStatus;

    private ProductActiveStatus activeStatus;

    private String color;

    private String hexCode;

    //    private Date itemCreated;

//    private Date itemUpdated;

}
