package com.menzo.Admin_Service.Dto;

import java.util.ArrayList;
import java.util.List;

public class NestedVariationDto {

    private Long id;
    private String variationName;
    private List<NestedVariationDto> options = new ArrayList<>();

    public NestedVariationDto(){}

    public NestedVariationDto(Long id, String variationName) {
        this.id = id;
        this.variationName = variationName;
    }

    public NestedVariationDto(Long id, String variationName, List<NestedVariationDto> options){
        this.id = id;
        this.variationName = variationName;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariationName() {
        return variationName;
    }

    public void setVariationName(String variationName) {
        this.variationName = variationName;
    }

    public List<NestedVariationDto> getOptions() {
        return options;
    }

    public void setOptions(List<NestedVariationDto> options) {
        this.options = options;
    }
}
