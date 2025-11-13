package com.menzo.Product_Service.Dto.FilterDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryDetailsDto {

    private Integer page;

    private Integer size;

    private String sortRequest;

    private boolean allowInactiveProductItems;

    private List<FilterRequestDto> filterValues;

    private Map<String, Integer> statusFlags;

}
