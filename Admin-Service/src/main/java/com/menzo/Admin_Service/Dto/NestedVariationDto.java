package com.menzo.Admin_Service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NestedVariationDto {

    private Long id;

    private String variationName;

    private List<NestedVariationDto> options = new ArrayList<>();

}
