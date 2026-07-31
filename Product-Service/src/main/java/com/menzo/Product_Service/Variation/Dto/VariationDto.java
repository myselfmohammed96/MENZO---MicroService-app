package com.menzo.Product_Service.Variation.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariationDto {

    private Long variationId;

    private String variationName;

    private LocalDateTime createdAt;

}
