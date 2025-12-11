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

    private String categoryName;
    //  only one category at a time
    //  check for existance in product retrieval servie

    private List<String> subCategoryNames;
    //  can be multiple at a time

    private boolean allowInactiveProductItems;

    private List<FilterRequestDto> filterValues;

    private Map<String, Integer> statusFlags;

}
