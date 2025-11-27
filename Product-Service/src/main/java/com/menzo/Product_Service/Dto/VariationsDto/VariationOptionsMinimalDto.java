package com.menzo.Product_Service.Dto.VariationsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariationOptionsMinimalDto {

    private Long variationId;

    private String variationName;

    private List<OptionMinimalDto> options;

}
