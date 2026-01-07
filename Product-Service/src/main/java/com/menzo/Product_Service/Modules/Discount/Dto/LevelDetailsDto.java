package com.menzo.Product_Service.Modules.Discount.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LevelDetailsDto {

    private Long id;

    private String text;

    private String imageIcon;

    private String size;

    private String color;

    private String hexCode;

}
