package com.menzo.Product_Service.Modules.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductDto {

    private String productName;

    private String description;

    private Long categoryId;

    private Long subCategoryId;


    private Long colorId;

    private String status;

    private String pod;


    private String discount;

    private String discountType;

    private Float itemWeight;

    private String genericName;

    private String countryOfOrigin;

    private String manufacturer;

    private String packer;

}
