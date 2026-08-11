package com.menzo.Product_Service.Category.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private UUID categoryId;

    private UUID parentCategoryId;

    private String categoryName;

    private Boolean isActive;

    private Instant createdAt;

}
