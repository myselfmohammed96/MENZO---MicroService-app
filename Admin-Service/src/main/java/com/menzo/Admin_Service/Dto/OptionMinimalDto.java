package com.menzo.Admin_Service.Dto;

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

}
