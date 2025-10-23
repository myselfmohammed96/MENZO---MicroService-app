package com.menzo.Product_Service.Dto.SpecificationsDto;

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
