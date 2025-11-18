package com.menzo.Product_Service.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDetailsDto {

    private String superSku;

    private Float basePrice;

    private String color;

    private String hexCode;

    private Date itemCreated;

    private Date itemUpdated;

    private List<String> imageUrls;

    private List<ItemSizeDto> sizeDetails;

}
