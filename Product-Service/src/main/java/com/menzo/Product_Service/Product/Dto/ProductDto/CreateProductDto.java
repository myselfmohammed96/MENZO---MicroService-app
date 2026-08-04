package com.menzo.Product_Service.Product.Dto.ProductDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductDto {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product code is required")
    private String productCode;

    @NotBlank(message = "Product parent category id required")
    private Long categoryId;

    @NotBlank(message = "Product sub-category id required")
    private Long subCategoryId;

    @NotBlank(message = "Product description required")
    private String description;

    private String podAvailable;     // ## must be boolean

    private String activeStatus;

    @NotBlank(message = "Product color required")
    private Long colorId;

    private String discount;

    private String discountType;

    private String genericName;

    private Float itemWeight;

    private String manufacturer;

    private String packer;

    @NotBlank(message = "Product country of origin required")
    private String countryOfOrigin;

}
