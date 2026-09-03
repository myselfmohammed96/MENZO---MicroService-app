package com.menzo.Product_Service.Product.Dto.ItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemMinDto {

    private UUID itemId;

    private String sku;

    private String imageUrl;

    private String size;

    private String colorName;

    private String hexCode;

}
