package com.menzo.Product_Service.Product.Dto.ProductDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

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
    private UUID categoryId;

    @NotBlank(message = "Product sub-category id required")
    private UUID subCategoryId;

    @NotBlank(message = "Product description required")
    private String description;

    private String podAvailable;     // ## must be boolean

    private String activeStatus;

    @NotBlank(message = "Product color required")
    private UUID colorId;

    private String discount;

    private String discountType;

    private String genericName;

    private BigDecimal itemWeight;

    private String manufacturer;

    private String packer;

    @NotBlank(message = "Product country of origin required")
    private String countryOfOrigin;

}
