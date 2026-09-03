package com.menzo.Product_Service.SearchAndFilter.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    //  check for existence in product retrieval service

    private List<String> subCategoryNames;
    //  can be multiple at a time

    private boolean allowInactiveProductItems;

    private List<FilterRequestDto> filterValues;

    private Map<String, Integer> statusFlags;

    private List<UUID> searchResultProductIds;

}
