package com.menzo.Product_Service.Modules.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductItemDto {

    private Long productId;

    private  Long colorId;

    private String status;

//    private List<SizeDetailsDto> sizeDetails;

}
