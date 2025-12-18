package com.menzo.Product_Service.Modules.Variation.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariationDto {

    private Long variationId;

    private String variationName;

    private Date createdAt;

}
