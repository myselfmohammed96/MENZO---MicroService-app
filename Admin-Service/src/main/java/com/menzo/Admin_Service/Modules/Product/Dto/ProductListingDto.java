package com.menzo.Admin_Service.Modules.Product.Dto;

import com.menzo.Admin_Service.Enum.ActiveStatus;
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

    private ActiveStatus activeStatus;

    private String iconImage;

}
