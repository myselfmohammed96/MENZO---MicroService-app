package com.menzo.Product_Service.Modules.SearchAndFilter.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterRequestDto {

    private String filterType;

    private String values;

}
