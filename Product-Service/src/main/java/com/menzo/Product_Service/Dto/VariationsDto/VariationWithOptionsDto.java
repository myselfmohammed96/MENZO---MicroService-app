package com.menzo.Product_Service.Dto.VariationsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariationWithOptionsDto {

    private Long id;

    private String variationName;

    private Set<OptionWithIdDto> options = new HashSet<>();

}
