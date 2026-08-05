package com.menzo.Product_Service.Product.Dto.ItemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceDto {

    private BigDecimal sellingPrice;

    private BigDecimal mrp;

}
