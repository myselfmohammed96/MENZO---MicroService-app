package com.menzo.Product_Service.Product.Dto;

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
public class UserItemListingDto {

    private String superSku;

    private String colorName;

    private String iconImage;

//    private Float price;

    private BigDecimal mrp;

    private BigDecimal sellingPrice;

    private List<String> sizes;

}
