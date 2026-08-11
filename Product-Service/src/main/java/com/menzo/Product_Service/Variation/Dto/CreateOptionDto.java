package com.menzo.Product_Service.Variation.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOptionDto {

    @NotBlank(message = "Variation option value is required")
    private String optionValue;

    @NotNull(message = "Variation is required")
    private UUID variationId;

    private String hexCode;

}
