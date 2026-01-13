package com.menzo.Product_Service.Modules.Product.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeDetailsDto {

    private Long sizeId;

    private String sizeValue;

    private Integer sizeStock;

    private BigDecimal sizeMrp;

    private BigDecimal sizeSellingPrice;

}
