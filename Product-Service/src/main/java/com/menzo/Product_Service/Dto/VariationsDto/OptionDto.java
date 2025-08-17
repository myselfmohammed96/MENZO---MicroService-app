package com.menzo.Product_Service.Dto.VariationsDto;

import com.menzo.Product_Service.Entity.Variation;

import java.util.Date;

public class OptionDto {

    private Long id;
    private String optionValue;
    private Variation variation;
    private Date createdAt;

    public OptionDto() {}

    public OptionDto(String optionValue, Variation variation) {
        this.optionValue = optionValue;
        this.variation = variation;
    }

    public OptionDto(Long id, String optionValue, Variation variation, Date createdAt) {
        this.id = id;
        this.optionValue = optionValue;
        this.variation = variation;
        this.createdAt = createdAt;
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

    public Variation getVariation() {
        return variation;
    }

    public void setVariation(Variation variation) {
        this.variation = variation;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void display() {
        System.out.println("OptionDto:\nid: " + id + "\noptionValue: " + optionValue +
                "\nvariation: " + variation + "\ncreatedAt: " + createdAt);
    }
}
