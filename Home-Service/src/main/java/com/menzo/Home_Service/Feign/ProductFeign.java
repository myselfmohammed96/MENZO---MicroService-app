package com.menzo.Home_Service.Feign;

import com.menzo.Home_Service.Dto.CategoryMinimalDto;
import com.menzo.Home_Service.Dto.ParentCategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE", url = "http://localhost:8986")
public interface ProductFeign {

    @GetMapping("/categories/get-all-with-banner")
    public List<CategoryMinimalDto> getAllCategoriesWithBanner();

    @GetMapping("/categories/get-parent")
    public ParentCategoryDto getParentCategoryById(@RequestParam("id") Long parentCategoryId);

    @GetMapping("/categories/get-sub-with-banner")
    public List<CategoryMinimalDto> getAllSubCategoriesWithBanner(@RequestParam("id") Long parentId);
}
