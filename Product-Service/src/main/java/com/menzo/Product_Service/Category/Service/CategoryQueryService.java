package com.menzo.Product_Service.Category.Service;

import com.menzo.Product_Service.Category.Dto.*;
import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableCategoryFilter;
import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Category.Repository.CategoriesRepository;
import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Constants.DbConstant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryQueryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryQueryService.class);

    @Autowired
    private CategoriesRepository categoriesRepo;

    @PersistenceContext
    private EntityManager entityManager;


    /*
     *
     *   Get all parent categories
     *   Without sub-categories
     *
     */
    @Transactional
    @EnableCategoryFilter
    public List<CategoryDto> getAllParents() {
//        Session session = entityManager.unwrap(Session.class);
//        session.enableFilter("activeFilter")
//                .setParameter("isDeleted", false);
        //  fetch parent categories
        List<ProductCategory> categoriesList = categoriesRepo.findByParentCategory_CategoryIdIsNull();

        return categoriesList.stream()
                .map(p -> CategoryDto.builder()
                        .categoryId(p.getCategoryId())
                        .parentCategoryId(p.getParentCategory().getCategoryId())
                        .categoryName(p.getCategoryName())
                        .isActive(p.isActive())
                        .createdAt(p.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());
    }


    /*
     *
     *   Get all parent categories
     *   With sub-categories
     *
     */
    public List<NestedCategoryDto> getAllParentWithSub() {

        //  fetch parent categories data
        List<Object[]> results = categoriesRepo.findAllParentWithSub(
                DbConstant.TRUE,
                DbConstant.FALSE,
                DbConstant.TRUE,
                DbConstant.FALSE
        );

        //  organize data into Dto
        Map<Long, NestedCategoryDto> parentMap = new HashMap<>();

        for (Object[] result : results) {
            Long parentId = (Long) result[0];
            String parentName = (String) result[1];
            Long subId = (Long) result[2];
            String subName = (String) result[3];

            NestedCategoryDto parentCategory = parentMap.computeIfAbsent(parentId, id -> NestedCategoryDto.builder()
                    .id(id)
                    .categoryName(parentName)
                    .subCategories(new ArrayList<NestedCategoryDto>())
                    .build());
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


    /*
     *
     *   Get parent category
     *   Parent category identified by ID
     *   Returns parent category as entity
     *   ?? for internal use ??
     *
     */
    @Transactional
    @EnableCategoryFilter
    public ProductCategory getParentCategoryEntityById(Long parentId) {
        return categoriesRepo.findByIdAndParentCategory_CategoryIdIsNull(parentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentId));
    }


    /*
     *
     *   Get parent category details
     *   Parent category identified by ID
     *   Returns parent category as DTO
     *
     */
    @Transactional
    @EnableCategoryFilter
    public CategoryDto getParentCategoryById(Long parentId) {
        ProductCategory parent = categoriesRepo.findByIdAndParentCategory_CategoryIdIsNull(parentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentId));
        return CategoryDto.builder()
                .categoryId(parent.getCategoryId())
                .categoryName(parent.getCategoryName())
                .isActive(parent.isActive())
                .createdAt(parent.getCreatedAt())
                .build();
    }


    /*
     *
     *   Get parent category with sub-categories
     *   Parent category identified by category ID
     *
     */
    public NestedCategoryDto getParentCategoryByIdWithSub(Long parentCategoryId) {
        //  fetch parent category
        List<Object[]> results = categoriesRepo.findParentByIdWithSub(
                parentCategoryId,
                DbConstant.TRUE,
                DbConstant.FALSE,
                DbConstant.TRUE,
                DbConstant.FALSE
        );

        //  organize data in Dto
        Map<Long, NestedCategoryDto> parentMap = new HashMap<>();

        for (Object[] result : results) {
            Long parentId = (Long) result[0];
            String parentName = (String) result[1];
            Long subId = (Long) result[2];
            String subName = (String) result[3];

            NestedCategoryDto parentCategory = parentMap.computeIfAbsent(parentId, id -> NestedCategoryDto.builder()
                    .id(id)
                    .categoryName(parentName)
                    .subCategories(new ArrayList<NestedCategoryDto>())
                    .build());
            if (subId != null) {
                parentCategory.getSubCategories().add(
                        NestedCategoryDto.builder()
                                .id(subId)
                                .categoryName(subName)
                                .build()
                );
            }
        }
        if (parentMap.size() != 1) return null;     // ## raise an exception here
        return parentMap.values().iterator().next();
    }


    /*
     *
     *   Get all Parent categories
     *   With banner images
     *
     */
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


    /*
     *
     *   Get parent category
     *   Parent category identified by sub-category ID
     *
     */
    public ParentCategoryView getParentBySubCategoryId(Long subCategoryId) {
        return categoriesRepo.findParentCategoryBySubId(
                DbConstant.TRUE,
                DbConstant.FALSE,
                DbConstant.TRUE,
                DbConstant.FALSE,
                subCategoryId
        );
    }


    /*
     *
     *   Get parent category
     *   Parent category identified by product ID
     *
     */
    public ParentCategoryView getParentByProductId(Long productId) {
        return categoriesRepo.findParentByProductId(
                DbConstant.TRUE,
                DbConstant.FALSE,
                DbConstant.TRUE,
                DbConstant.FALSE,
                DbConstant.TRUE,
                DbConstant.FALSE,
                productId
        );
    }


//    ********* Sub categories *********


    /*
     *
     *   Get all sub-categories without variations
     *   Sub-categories identified by parent category ID
     *
     */
    @Transactional
    @EnableCategoryFilter
    public List<CategoryDto> getAllSubCategoriesByParentId(Long parentId) {
        if (!categoriesRepo.existsById(parentId)) {
            logger.error("Parent category not found with ID: {}", parentId);
            throw new EntityNotFoundException("Parent category not found with ID: " + parentId);
        }
        List<ProductCategory> subCategories = categoriesRepo.findAllByParentCategory_CategoryId(parentId);
        logger.info("Fetching {} sub-categories with parent ID {}", subCategories.size(), parentId);

        return subCategories.stream()
                .map(s -> CategoryDto.builder()
                        .categoryId(s.getCategoryId())
                        .parentCategoryId(s.getParentCategory().getCategoryId())
                        .categoryName(s.getCategoryName())
                        .isActive(s.isActive())
                        .createdAt(s.getCreatedAt())
                        .build()
                ).collect(Collectors.toList());
    }


    /*
     *
     *   Get sub-category without variations
     *   Sub-category identified by sub-category ID
     *   Returns ProductCategory Entity
     *
     */
    @Transactional
    @EnableCategoryFilter
    public ProductCategory getSubCategoryEntityById(Long subCategoryId) {
        logger.info("Fetching sub-category with ID: {}", subCategoryId);
        return categoriesRepo.findByIdAndParentCategory_CategoryIdIsNotNull(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Sub-category not found with ID: " + subCategoryId));
    }


    /*
     *
     *   Get sub-category without variations
     *   Sub-category identified by sub-category ID
     *   Returns data in Dto
     *
     */
    @Transactional
    @EnableCategoryFilter
    public CategoryDto getSubCategoryById(Long subCategoryId) {
        ProductCategory sub = categoriesRepo.findByIdAndParentCategory_CategoryIdIsNotNull(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Sub-category not found with ID: " + subCategoryId));
        return CategoryDto.builder()
                .categoryId(sub.getCategoryId())
                .parentCategoryId(sub.getParentCategory().getCategoryId())
                .categoryName(sub.getCategoryName())
                .isActive(sub.isActive())
                .createdAt(sub.getCreatedAt())
                .build();
    }


    /*

     *
     *   Get all sub-categories with banner images
     *   Sub-categories identified by parent category ID
     *
     */
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