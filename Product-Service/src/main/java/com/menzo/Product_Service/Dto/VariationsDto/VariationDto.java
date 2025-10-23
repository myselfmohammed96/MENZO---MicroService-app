package com.menzo.Product_Service.Dto.VariationsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariationDto {

    private Long id;

    private String variationName;

    private Date createdAt;

}
