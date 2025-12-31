package com.menzo.Product_Service.Modules.Discount.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MappedContentDto {

    private UUID mappingId;

    private String textContent;

//    private DiscountLevel level;

    private String size;

    private String color;

    private String hexCode;

    private Integer exclusionCount;

}
