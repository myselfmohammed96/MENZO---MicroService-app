package com.menzo.Product_Service.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductItemDto {

    private Long productId;

    private  Long colorId;

    private String status;

//    private List<SizeDetailsDto> sizeDetails;

}
