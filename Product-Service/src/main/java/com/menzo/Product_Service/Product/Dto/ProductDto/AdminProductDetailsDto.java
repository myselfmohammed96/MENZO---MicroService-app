package com.menzo.Product_Service.Product.Dto.ProductDto;

import com.menzo.Product_Service.Product.Dto.ItemDto.AdminItemListingDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminProductDetailsDto {

    private String productName;

    private String categoryName;

    private String subCategoryName;

    private String description;

    private boolean pod;

    private LocalDateTime productCreated;

    private LocalDateTime productUpdated;

    private BigDecimal itemWeight;

    private String genericName;

    private String countryOfOrigin;

    private String manufacturer;

    private String packer;

    private List<AdminItemListingDto> productItems;

}
