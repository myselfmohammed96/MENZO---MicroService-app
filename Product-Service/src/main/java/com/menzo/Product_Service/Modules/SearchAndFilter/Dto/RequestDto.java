package com.menzo.Product_Service.Modules.SearchAndFilter.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDto {

    private List<FilterRequestDto> filterRequestDtos;

    public enum filterFields {
        optionValue,
        variationName;
    }

}
