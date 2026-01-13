package com.menzo.Product_Service.Modules.Product.Dto;

import com.menzo.Product_Service.Modules.Product.Enum.ProductActiveStatus;
import com.menzo.Product_Service.Modules.Product.Enum.StockStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetailsDto {


    private BigDecimal baseMrp;

    private BigDecimal baseSellingPrice;

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
