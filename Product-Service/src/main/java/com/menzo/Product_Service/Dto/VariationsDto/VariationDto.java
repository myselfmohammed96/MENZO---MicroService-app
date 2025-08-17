package com.menzo.Product_Service.Dto.VariationsDto;

import java.util.Date;

public class VariationDto {

    private Long id;
    private String variationName;
    private Date createdAt;

    public VariationDto(){}

    public VariationDto(String variationName) {
        this.variationName = variationName;
    }

    public VariationDto(Long id, String variationName) {
        this.id = id;
        this.variationName = variationName;
    }

    public VariationDto(Long id, String variationName, Date createdAt) {
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
        System.out.println("VariationDto:\nid: " + id + "\nvariationName: " + variationName +
                "\ncreatedAt: " + createdAt);
    }
}
