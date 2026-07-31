package com.menzo.Product_Service.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemDetailsDto {

    private Long productItemId;

    private String productName;

    private String categoryName;

    private String subCategoryName;

    private String productDescription;

    private boolean podAvailable;

    private Date addedDate;


    private String sku;

    private Integer stockQty;

    private Float price;

    private String color;

    private String size;

    private boolean isActive;

//    private Float itemWeight;
//    private String genericName;
//    private String countryOfOrigin;
//    private String manufacturer;
//    private String packer;

}
