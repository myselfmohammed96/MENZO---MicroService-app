package com.menzo.Product_Service.Dto.CategoriesDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCategoryDto {

    private Long id;

    private Long parentCategoryId;

    private String categoryName;

    private Boolean isActive;

    private Instant createdAt;

}
