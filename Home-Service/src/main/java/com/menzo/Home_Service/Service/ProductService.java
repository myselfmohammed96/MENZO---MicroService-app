package com.menzo.Home_Service.Service;

import com.menzo.Home_Service.Dto.CategoryMinimalDto;
import com.menzo.Home_Service.Dto.ParentCategoryDto;
import com.menzo.Home_Service.Feign.ProductFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductFeign productFeign;

    public List<CategoryMinimalDto> getAllCategories() {
        List<CategoryMinimalDto> allCategories = productFeign.getAllCategoriesWithBanner();
        return allCategories;
    }

    public ParentCategoryDto getParentCategoryById(Long id) {
        ParentCategoryDto parentCategory = productFeign.getParentCategoryById(id);
        return parentCategory;
    }

    public List<CategoryMinimalDto> getAllSubCategoriesByCategoryId(Long id) {
        List<CategoryMinimalDto> allSubCategories = productFeign.getAllSubCategoriesWithBanner(id);
        return allSubCategories;
    }
}
