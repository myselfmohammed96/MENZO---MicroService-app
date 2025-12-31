package com.menzo.Product_Service.Modules.Discount.Enum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnumDto {

    private String enumName;

    private List<String> enumValues;

}
