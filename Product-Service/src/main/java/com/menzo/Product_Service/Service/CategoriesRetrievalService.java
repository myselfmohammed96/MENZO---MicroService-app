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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoriesRetrievalService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriesRetrievalService.class);

    @Autowired
    private CategoriesRepo categoriesRepo;

    @Autowired
    private VariationsRepo variationsRepo;

    @Autowired
    private ProductsRepo productsRepo;



//    ********* Parent categories *********

    //  Get all parent categories - without sub-categories (id, categoryName, isActive, createdAt)
    //  TESTED
    public List<ParentCategoryDto> getAllParents() {
        List<ProductCategory> categoriesList = categoriesRepo.findByParentCategoryIdIsNull();
        logger.info("Fetched {} parent categories", categoriesList.size());

        return categoriesList.stream()
                .map(p -> {
                    return new ParentCategoryDto(
                            p.getId(),
                            p.getCategoryName(),
                            p.getIsActive(),
                            p.getCreatedAt()
                    );
                }).collect(Collectors.toList());
    }

    //  Get all parent categories - with sub-categories (id, categoryName, List<SubCategories> -> (id, categoryName))
    //  TESTED
    public List<NestedCategoryDto> getAllParentWithSub() {
        List<Object[]> results = categoriesRepo.findAllParentWithSub();
        Map<Long, NestedCategoryDto> parentMap = new HashMap<>();
        for (Object[] result : results) {
            Long parentId = (Long) result[0];
            String parentName = (String) result[1];
            Long subId = (Long) result[2];
            String subName = (String) result[3];

            NestedCategoryDto parentCategory = parentMap.computeIfAbsent(parentId, id -> {
                return NestedCategoryDto.builder()
                        .id(id)
                        .categoryName(parentName)
                        .subCategories(new ArrayList<NestedCategoryDto>())
                        .build();
            });
            if (subId != null) {
                NestedCategoryDto sub = NestedCategoryDto.builder()
                        .id(subId)
                        .categoryName(subName)
                        .build();
                parentCategory.getSubCategories().add(sub);
            }
        }
        return new ArrayList<>(parentMap.values());
    }

    //  Get parent category by id - without sub-categories (id, categoryName, isActive, createdAt)  ---@RequestHeader("roles") String roles,
    //  TESTED
    public ParentCategoryDto getParentCategoryById(Long parentCategoryId) {
        ProductCategory parentCategory = categoriesRepo.findByIdAndParentCategoryIdIsNull(parentCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentCategoryId));
        logger.info("Found parent category with ID: {}", parentCategoryId);
        return new ParentCategoryDto(
                parentCategory.getId(),
                parentCategory.getCategoryName(),
                parentCategory.getIsActive(),
                parentCategory.getCreatedAt()
        );
    }

    //  Get parent category by id - with sub-categories (id, categoryName, List<SubCategories> -> (id, categoryName))  ---@RequestHeader("roles") String roles,
    //  TESTED
    public NestedCategoryDto getParentCategoryByIdWithSub(Long parentCategoryId) {
        List<Object[]> results = categoriesRepo.findParentByIdWithSub(parentCategoryId);
        Map<Long, NestedCategoryDto> parentMap = new HashMap<>();
        for (Object[] result : results) {
            System.out.println(Arrays.toString(result));
            Long parentId = (Long) result[0];
            String parentName = (String) result[1];
            Long subId = (Long) result[2];
            String subName = (String) result[3];

            NestedCategoryDto parentCategory = parentMap.computeIfAbsent(parentId, id -> {
                return NestedCategoryDto.builder()
                        .id(id)
                        .categoryName(parentName)
                        .subCategories(new ArrayList<NestedCategoryDto>())
                        .build();
            });
            if (subId != null) {
                parentCategory.getSubCategories().add(
                        NestedCategoryDto.builder()
                                .id(subId)
                                .categoryName(subName)
                                .build()
                );
            }
        }
        if (parentMap.size() != 1) return null;
        return parentMap.values().iterator().next();
    }

    //  Get parent category by sub category id
    //  TESTED
    public ParentCategoryView getParentBySubCategoryId(Long subCategoryId) {
        return categoriesRepo.findParentCategoryBySubId(subCategoryId);
    }



//    ********* Sub categories *********

    //  Get all sub category by parent id - without variations (id, parentCategoryId, categoryName, isActive, createdAt)
    //  TESTED
    public List<SubCategoryDto> getAllSubOfParentId(Long parentId) {
        if (!categoriesRepo.existsById(parentId)) {
            logger.error("Parent category not found with ID: {}", parentId);
            throw new EntityNotFoundException("Parent category not found with ID: " + parentId);
        }
        List<ProductCategory> subCategories = categoriesRepo.findAllByParentCategoryId(parentId);
        logger.info("Fetching {} sub-categories with parent ID {}", subCategories.size(), parentId);

        return subCategories.stream()
                .map(s -> new SubCategoryDto(
                        s.getId(),
                        s.getParentCategoryId(),
                        s.getCategoryName(),
                        s.getIsActive(),
                        s.getCreatedAt()
                )).collect(Collectors.toList());
    }

    //  Get sub category by id - without variations (id, parentCategoryId, categoryName, isActive, createdAt)
    //  TESTED
    public SubCategoryDto getSubCategoryById(Long subCategoryId) {
        logger.info("Fetching sub-category with ID: {}", subCategoryId);
        ProductCategory subCategory = categoriesRepo.findByIdAndParentCategoryIdIsNotNull(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Sub category not found with ID: " + subCategoryId));

        return new SubCategoryDto(
                subCategory.getId(),
                subCategory.getParentCategoryId(),
                subCategory.getCategoryName(),
                subCategory.getIsActive(),
                subCategory.getCreatedAt()
        );
    }

//    public List<CategoryMinimalDto> getAllCategoriesWithBanner() {
//        List<ParentCategoryDto> allParentCategories = getAllParents();
//        return allParentCategories.stream()
//                .map(parent -> new CategoryMinimalDto(
//                        parent.getId(),
//                        parent.getCategoryName(),
//                        null)
//                )
//                .collect(Collectors.toList());
//    }

//    public List<CategoryMinimalDto> getAllSubCategoriesByParentIdWithBanner(Long parentId) {
//        List<SubCategoryDto> allSubByParentId = getAllSubOfParentId(parentId);
//        return allSubByParentId.stream()
//                .map(sub -> new CategoryMinimalDto(
//                        sub.getId(),
//                        sub.getCategoryName(),
//                        null)
//                )
//                .collect(Collectors.toList());
//    }

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
//                  return new SubCategoryDto(subCategory.getId(), subCategory.getParentCategoryId(), subCategory.getCategoryName(), subCategory.getIsActive(), subCategory.getCreatedAt());
//        return subCategory;
//    }