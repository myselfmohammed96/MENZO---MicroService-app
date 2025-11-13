package com.menzo.Product_Service.Dto.FilterDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterTypeDto {

    private String filterType;

    private String typeValue;

    private List<String> filterOptions;
}
