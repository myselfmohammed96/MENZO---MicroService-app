package com.menzo.Product_Service.Dto.ProductDto;

import com.menzo.Product_Service.Enum.ProductActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListingDto {

    private Long id;

    private String productName;

    private String subCategoryName;

    private Float startingPrice;

    private Integer totalItems;

    private ProductActiveStatus activeStatus;

    private String iconImage;

}
