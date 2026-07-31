package com.menzo.Product_Service.Variation.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVariationDto {

    private Long id;

    @NotBlank(message = "Variation name is required")
    private String variationName;

    private Date createdAt;

}
