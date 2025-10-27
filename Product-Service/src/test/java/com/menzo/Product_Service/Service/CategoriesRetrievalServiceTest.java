package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.NestedCategoryDto;
import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CategoriesRetrievalServiceTest {

    @Autowired
    private CategoriesRetrievalService categoriesRetrievalService;

    @Test
    public void testGetAllParents() {
        List<ParentCategoryDto> parents = categoriesRetrievalService.getAllParents();
        System.out.println(parents);
    }

    @Test
    public void testGetAllParentWithSub() {
        List<NestedCategoryDto> parents = categoriesRetrievalService
                .getAllParentWithSub();
        System.out.println(parents);
    }
}