package com.menzo.Product_Service.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private Float itemWeight;

    private String genericName;

    private String countryOfOrigin;

    private String manufacturer;

    private String packer;

    private Map<String, String> variations;

    private List<UserItemListingDto> items;

}
