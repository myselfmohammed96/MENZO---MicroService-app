package com.menzo.Product_Service.Dto.SpecificationsDto;

import java.util.List;

public class RequestDto {

    private List<FilterRequestDto> filterRequestDtos;

    public List<FilterRequestDto> getFilterRequestDtos() {
        return filterRequestDtos;
    }

    public void setFilterRequestDtos(List<FilterRequestDto> filterRequestDtos) {
        this.filterRequestDtos = filterRequestDtos;
    }

    public enum filterFields {
        optionValue,
        variationName;
    }

    public String toString() {
        return filterRequestDtos.toString();
    }

}
