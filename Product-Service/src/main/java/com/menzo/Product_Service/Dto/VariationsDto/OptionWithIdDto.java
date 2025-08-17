package com.menzo.Product_Service.Dto.VariationsDto;

import com.menzo.Product_Service.Entity.VariationOption;

public class OptionWithIdDto {
    private Long id;
    private String optionValue;

    public OptionWithIdDto() {}

    public OptionWithIdDto(Long id, String optionValue) {
        this.id = id;
        this.optionValue = optionValue;
    }

    public OptionWithIdDto(VariationOption option) {
        this.id = option.getId();
        this.optionValue = option.getOptionValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String toString() {
        return "OptionWithIdDto:\nid: " + id + "\noptionValue: " + optionValue + "\n";
    }
}
