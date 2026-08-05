package com.menzo.Product_Service.Product.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductItemDto {

    @NotNull
    @Positive
    private Long productId;

    @NotNull
    @Positive
    private  Long colorId;

    private String activeStatus;

//    private List<SizeDetailsDto> sizeDetails;

}
