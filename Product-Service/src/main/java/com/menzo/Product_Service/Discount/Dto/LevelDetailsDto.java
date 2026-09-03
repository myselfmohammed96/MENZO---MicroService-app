package com.menzo.Product_Service.Discount.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LevelDetailsDto {

    private UUID id;

    private String text;

    private String imageIcon;

    private String size;

    private String color;

    private String hexCode;

}
