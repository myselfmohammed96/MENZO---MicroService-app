package com.menzo.Product_Service.Variation.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionMinimalDto {

    private Long optionId;

    private String optionValue;

    private String colorCode;

    public OptionMinimalDto(Long optionId, String optionValue) {
        this.optionId = optionId;
        this.optionValue = optionValue;
    }

}
