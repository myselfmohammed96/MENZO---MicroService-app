package com.menzo.Product_Service.Modules.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserItemListingDto {

    private String superSku;

    private String colorName;

    private String iconImage;

    private Float price;

    private List<String> sizes;

}
