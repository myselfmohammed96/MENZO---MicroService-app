package com.menzo.Product_Service.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailsDto {

    private String productName;

    private String categoryName;

    private String subCategoryName;

    private String description;

    private boolean pod;

    private Date productCreated;

    private Date productUpdated;

    private Float itemWeight;

    private String genericName;

    private String countryOfOrigin;

    private String manufacturer;

    private String packer;

    private List<ItemListingDto> productItems;

}
