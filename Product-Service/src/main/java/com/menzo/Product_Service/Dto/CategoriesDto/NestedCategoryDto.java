package com.menzo.Product_Service.Dto.CategoriesDto;

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
public class NestedCategoryDto {

    private Long id;

    private String categoryName;

    private List<NestedCategoryDto> subCategories = new ArrayList<>();

}
