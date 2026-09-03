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
public class ItemImageDto {

    private UUID productImageId;

    private String imageUrl;

    private int imageOrder;

    private boolean isPrimaryImage;

}
