package com.menzo.Product_Service.Variation.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDto {

    private UUID optionId;

    private String optionValue;

    private String colorCodeHex;

    public OptionDto(UUID optionId, String optionValue) {
        this.optionId = optionId;
        this.optionValue = optionValue;
    }

}
