package com.menzo.Product_Service.Dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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
