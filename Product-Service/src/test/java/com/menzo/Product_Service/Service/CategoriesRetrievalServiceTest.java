package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.NestedCategoryDto;
import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryDto;
import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryView;
import com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto;
import com.menzo.Product_Service.Entity.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CategoriesRetrievalServiceTest {

    @Autowired
    private CategoriesRetrievalService categoriesRetrievalService;



    //    ********* Parent categories *********

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

    @Test
    public void testGetParentCategoryById() {
        ParentCategoryDto parent = categoriesRetrievalService.getParentCategoryById(2L);
        System.out.println(parent);
    }

    @Test
    public void testGetParentCategoryByIdWithSub() {
        NestedCategoryDto parent = categoriesRetrievalService.getParentCategoryByIdWithSub(2L);
        System.out.println(parent);
    }

    @Test
    public void testGetParentBySubCategoryId() {
        ParentCategoryView parent = categoriesRetrievalService.getParentBySubCategoryId(121L);
        System.out.println(parent.getId() + " - " + parent.getCategoryName());
    }



    //    ********* Sub categories *********

    @Test
    public void testGetAllSubOfParentId() {
        List<SubCategoryDto> subList = categoriesRetrievalService.getAllSubOfParentId(2L);
        System.out.println(subList);
    }

    @Test
    public void testGetSubCategoryById() {
        ProductCategory sub = categoriesRetrievalService.getSubCategoryById(121L);
        System.out.println(sub);
    }

}