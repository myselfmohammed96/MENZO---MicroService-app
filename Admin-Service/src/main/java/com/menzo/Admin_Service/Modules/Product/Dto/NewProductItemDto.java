package com.menzo.Admin_Service.Modules.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductItemDto {

    private Long productId;

    private String status;

    private Long colorId;

    private List<SizeDetailsDto> sizeDetails;

}
