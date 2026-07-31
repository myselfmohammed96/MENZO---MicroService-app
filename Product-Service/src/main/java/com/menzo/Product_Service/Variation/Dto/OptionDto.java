package com.menzo.Product_Service.Variation.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDto {

    private Long optionId;

    private String optionValue;

    private String colorHexCode;

    private Long variationId;

    private String variationName;

    private Date createdAt;

}
