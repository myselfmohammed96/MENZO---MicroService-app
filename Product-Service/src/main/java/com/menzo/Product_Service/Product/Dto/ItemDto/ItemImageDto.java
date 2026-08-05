package com.menzo.Product_Service.Product.Dto.ItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemImageDto {

    private Long productImageId;

    private String imageUrl;

    private int imageOrder;

    private boolean isPrimaryImage;

}
