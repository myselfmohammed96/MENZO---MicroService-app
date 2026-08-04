package com.menzo.Product_Service.Product.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull
    @Positive
    private Long sizeId;

    @NotBlank
    private String sizeValue;

    @NotNull
    @Positive
    private Integer sizeStock;

    @NotNull
    @Positive
    private BigDecimal sizeMrp;

    @NotNull
    @Positive
    private BigDecimal sizeSellingPrice;

}
