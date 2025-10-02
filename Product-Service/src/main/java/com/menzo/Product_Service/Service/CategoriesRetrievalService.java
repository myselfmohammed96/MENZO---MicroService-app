package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.CategoriesDto.*;
import com.menzo.Product_Service.Entity.ProductCategory;
import com.menzo.Product_Service.Repository.CategoriesRepo;
import com.menzo.Product_Service.Repository.ProductsRepo;
import com.menzo.Product_Service.Repository.VariationsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoriesRetrievalService {

    private static final Logger log = LoggerFactory.getLogger(CategoriesRetrievalService.class);

    @Autowired
    private CategoriesRepo categoriesRepo;

    @Autowired
    private VariationsRepo variationsRepo;

    @Autowired
    private ProductsRepo productsRepo;

//    Parent categories

    //    Get all parent categories - without sub-categories (id, categoryName, isActive, createdAt)
    public List<ParentCategoryDto> getAllParents() {
        List<ProductCategory> categoriesList = categoriesRepo.findAllParent();
        log.info("Fetched {} parent categories", categoriesList.size());
        List<ParentCategoryDto> parentCategoriesList = new ArrayList<>();
        for (ProductCategory p : categoriesList) {
            ParentCategoryDto parentCategory = new ParentCategoryDto(p.getId(), p.getCategoryName(), p.getIsActive(), p.getCreatedAt());
            parentCategoriesList.add(parentCategory);
            //p.display();    //** check **
        }
        return parentCategoriesList;
    }

    //    Get all parent categories - with sub-categories (id, categoryName, List<SubCategories> -> (id, categoryName))
    public List<NestedCategoryDto> getAllParentWithSub(){
        List<Object[]> results = categoriesRepo.findAllParentWithSub();
        Map<Long, NestedCategoryDto> parentMap = new HashMap<>();
        for (Object[] result: results){
            Long parentId = (Long) result[0];
            String parentName = (String) result[1];
            Long subId = (Long) result[2];
            String subName = (String) result[3];

            NestedCategoryDto parentCategory = parentMap.computeIfAbsent(parentId, id ->
                    new NestedCategoryDto(id, parentName));

            if (subId != null) {
                parentCategory.getSubCategories().add(new NestedCategoryDto(subId, subName));
            }
        }
        List<NestedCategoryDto> categoriesList = new ArrayList<>(parentMap.values());
        return categoriesList;
    }

    //    Get parent category by id - without sub-categories (id, categoryName, isActive, createdAt)  ---@RequestHeader("roles") String roles,
    public ParentCategoryDto getParentCategoryById(Long parentCategoryId) {
        ProductCategory parentCategory = categoriesRepo.findParentById(parentCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentCategoryId));
        log.info("Found parent category with ID: {}", parentCategoryId);
        parentCategory.display();   // **check**
        return new ParentCategoryDto(parentCategory.getId(), parentCategory.getCategoryName(), parentCategory.getIsActive(), parentCategory.getCreatedAt());
    }

    //    Get parent category by id - with sub-categories (id, categoryName, List<SubCategories> -> (id, categoryName))  ---@RequestHeader("roles") String roles,
    public NestedCategoryDto getParentCategoryByIdWithSub(Long parentCategoryId) {
        List<Object[]> results = categoriesRepo.findParentByIdWithSub(parentCategoryId);
        Map<Long, NestedCategoryDto> parentMap = new HashMap<>();
        for (Object[] result: results) {
            Long parentId = (Long) result[0];
            String parentName = (String) result[1];
            Long subId = (Long) result[2];
            String subName = (String) result[3];

            NestedCategoryDto parentCategory = parentMap.computeIfAbsent(parentId, id ->
                    new NestedCategoryDto(id, parentName));
            if (subId != null) {
                parentCategory.getSubCategories().add(new NestedCategoryDto(subId, subName));
            }
        }
        if (parentMap.size() != 1) {
            return null;
        }
        NestedCategoryDto parentCategoryWithSub = parentMap.values().iterator().next();
        return parentCategoryWithSub;
    }

    //    Get parent category by sub category id
    public ParentCategoryView getParentBySubCategoryId(Long subCategoryId) {
        ParentCategoryView parentCategory = categoriesRepo.findCategoryBySubId(subCategoryId);
        return parentCategory;
    }

//    Sub categories

    //    Get all sub category by parent id - without variations (id, parentCategoryId, categoryName, isActive, createdAt)
    public List<SubCategoryDto> getAllSubOfParentId(Long parentId) {
        if (!categoriesRepo.existsById(parentId)) {
            log.error("Parent category not found with ID: {}", parentId);
            throw new EntityNotFoundException("Parent category not found with ID: " + parentId);
        }
        List<SubCategoryDto> subCategoriesList = categoriesRepo.findAllSubByParentId(parentId);
        log.info("Fetched {} sub-categories with parent ID {}", subCategoriesList.size(), parentId);
        return subCategoriesList;
    }

    //    Get sub category by id - without variations (id, parentCategoryId, categoryName, isActive, createdAt)
    public SubCategoryDto getSubCategoryById(Long subCategoryId) {
        SubCategoryDto subCategory = categoriesRepo.findSubByIdWithoutVariation(subCategoryId);
//                .orElseThrow(() -> new EntityNotFoundException("Sub category not found with ID: " + subCategoryId));
        log.info("Found sub-category with ID: {}", subCategoryId);
        subCategory.display();  // *** check ***
        return subCategory;
    }

    public List<CategoryMinimalDto> getAllCategoriesWithBanner() {
        List<ParentCategoryDto> allParentCategories = getAllParents();
        return allParentCategories.stream().map(parent -> new CategoryMinimalDto(parent.getId(), parent.getCategoryName(), null))
                .collect(Collectors.toList());
    }

    public List<CategoryMinimalDto> getAllSubCategoriesByParentIdWithBanner(Long parentId) {
        List<SubCategoryDto> allSubByParentId = getAllSubOfParentId(parentId);
        return allSubByParentId.stream().map(sub -> new CategoryMinimalDto(sub.getId(), sub.getCategoryName(), null))
                .collect(Collectors.toList());
    }

//    public ProductCategory getSubByProductId(Long productId) {
//        Product product = productsRepo.findById(productId)
//                .orElseThrow(() -> new EntityNotFoundException("Product not found with product ID: " + productId));
//        product.display();  // *** check ***
//        product.getCategory().display();    // *** check ***
//        return product.getCategory();
//    }

//    //    Get sub category by id - with variations and variationOptions (id, parentCategoryId, categoryName, isActive, createdAt,
//    //                  Set<variations> -> (id, variationName, Set<variationOptions> -> (id, optionValue, variationId, createdAt), createdAt))
//    public ProductCategory getSubCategoryById(Long subCategoryId) {
//        ProductCategory subCategory = categoriesRepo.findSubById(subCategoryId)
//                .orElseThrow(() -> new EntityNotFoundException("Sub category not found with ID: " + subCategoryId));
//        log.info("Found subcategory with ID: {}", subCategoryId);
//        subCategory.display();
////        return new SubCategoryDto(subCategory.getId(), subCategory.getParentCategoryId(), subCategory.getCategoryName(), subCategory.getIsActive(), subCategory.getCreatedAt());
//        return subCategory;
//    }

}
