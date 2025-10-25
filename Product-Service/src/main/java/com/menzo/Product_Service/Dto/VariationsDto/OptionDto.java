package com.menzo.Product_Service.Dto.VariationsDto;

import com.menzo.Product_Service.Entity.Variation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDto {

    private Long id;

    private String optionValue;

    private String colorCode;

//    private Variation variation;

    private Date createdAt;

}
