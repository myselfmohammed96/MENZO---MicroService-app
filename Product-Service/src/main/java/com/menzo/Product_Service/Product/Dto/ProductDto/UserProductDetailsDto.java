package com.menzo.Product_Service.Product.Dto.ProductDto;

import com.menzo.Product_Service.Product.Dto.ItemDto.UserItemListingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProductDetailsDto {

    private String productName;

    private String description;

    private Boolean pod;

    private BigDecimal itemWeight;

    private String genericName;

    private String countryOfOrigin;

    private String manufacturer;

    private String packer;

    private Map<String, String> variations;

    private List<UserItemListingDto> items;

}
