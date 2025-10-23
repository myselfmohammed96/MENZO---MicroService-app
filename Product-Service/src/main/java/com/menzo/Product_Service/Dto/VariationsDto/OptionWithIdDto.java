package com.menzo.Product_Service.Dto.VariationsDto;

import com.menzo.Product_Service.Entity.VariationOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionWithIdDto {

    private Long id;

    private String optionValue;

}
