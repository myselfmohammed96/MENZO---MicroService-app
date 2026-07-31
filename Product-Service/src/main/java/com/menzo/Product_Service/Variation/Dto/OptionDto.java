package com.menzo.Product_Service.Variation.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDto {

    private Long optionId;

    private String optionValue;

    private String colorCodeHex;

}
