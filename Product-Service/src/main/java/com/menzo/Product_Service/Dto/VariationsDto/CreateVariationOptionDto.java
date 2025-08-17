package com.menzo.Product_Service.Dto.VariationsDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class CreateVariationOptionDto {

    private Long id;
    @NotBlank(message = "Variation option value is required")
    private String optionValue;
    @NotNull(message = "Variation is required")
    private Long variationId;
    private Date createdAt;

    public CreateVariationOptionDto(){}

    public CreateVariationOptionDto(Long id, String optionValue, Long variationId, Date createdAt){
        this.id = id;
        this.optionValue = optionValue;
        this.variationId = variationId;
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

    public Long getVariationId() {
        return variationId;
    }

    public void setVariationId(Long variationId) {
        this.variationId = variationId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void display() {
        System.out.println("CreateVariationOptionDto:\nid: " + id + "\noptionValue: " + optionValue +
                "\nvariationId: " + variationId + "\ncreatedAt: " + createdAt);
    }
}
