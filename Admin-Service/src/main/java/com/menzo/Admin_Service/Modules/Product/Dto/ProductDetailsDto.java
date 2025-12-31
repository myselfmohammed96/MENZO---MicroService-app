package com.menzo.Admin_Service.Modules.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailsDto {

    private Long productId;

    private String productName;

    private String categoryName;

    private String subCategoryName;

    private String productDescription;

    private boolean podAvailable;


    private Float itemWeight;

    private String genericName;

    private String countryOfOrigin;

    private String manufacturer;

    private String packer;

    private List<String> imageUrls = new ArrayList<>();

}
