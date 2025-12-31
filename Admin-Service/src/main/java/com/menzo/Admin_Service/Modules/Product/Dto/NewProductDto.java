package com.menzo.Admin_Service.Modules.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductDto {

    private String productName;
    private String description;
    private Map<Long, Integer> sizeStockMap;
    private Long color;
    private String status;
    private String pod;

    private Float price;
    //    discount
    //    discountType

    private Float itemWeight;
    private String genericName;
    private String countryOfOrigin;
    private String manufacturer;
    private String packer;

    private Long categoryId;
    private Long subCategoryId;

    //    variations

}
