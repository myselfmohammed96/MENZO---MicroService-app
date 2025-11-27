package com.menzo.Admin_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeDetailsDto {

    private Long sizeId;

    private String sizeValue;

    private Integer sizeStock;

    private Float sizePrice;

}
