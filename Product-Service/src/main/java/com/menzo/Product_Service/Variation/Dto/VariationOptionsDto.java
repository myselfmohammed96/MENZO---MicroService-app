package com.menzo.Product_Service.Variation.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariationOptionsDto {

    private Long variationId;

    private String variationName;

    private Set<OptionDto> options;

}
