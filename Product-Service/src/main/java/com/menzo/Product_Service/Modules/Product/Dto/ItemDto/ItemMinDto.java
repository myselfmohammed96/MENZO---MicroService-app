package com.menzo.Product_Service.Modules.Product.Dto.ItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemMinDto {

    private Long itemId;

    private String sku;

    private String imageUrl;

    private String size;

    private String colorName;

    private String hexCode;

}
