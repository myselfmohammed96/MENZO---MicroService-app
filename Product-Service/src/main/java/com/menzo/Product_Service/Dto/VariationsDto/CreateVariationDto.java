package com.menzo.Product_Service.Dto.VariationsDto;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class CreateVariationDto {

    private Long id;
    @NotBlank(message = "Variation name is required")
    private String variationName;
    private Date createdAt;

    public CreateVariationDto(){}

    public CreateVariationDto(String variationName){
        this.variationName = variationName;
    }

    public CreateVariationDto(Long id, String variationName, Date createdAt){
        this.id = id;
        this.variationName = variationName;
        this.createdAt = createdAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void display() {
        System.out.println("CreateVariationDto:\nid: " + id + "\nvariationName: " + variationName +
                "\ncreatedAt: " + createdAt);
    }
}
