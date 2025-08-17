package com.menzo.Product_Service.Dto.VariationsDto;

import java.util.HashSet;
import java.util.Set;

public class VariationWithOptionsDto {

    private Long id;
    private String variationName;
    private Set<OptionWithIdDto> options = new HashSet<>();

    public VariationWithOptionsDto() {}

    public VariationWithOptionsDto(Long id, String variationName, Set<OptionWithIdDto> options) {
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

    public Set<OptionWithIdDto> getOptions() {
        return options;
    }

    public void setOptions(Set<OptionWithIdDto> options) {
        this.options = options;
    }

    public void display() {
        System.out.println("VariationWithOptionsDto:\nid: " + id + "\nvariationName: " + variationName);
    }
}
