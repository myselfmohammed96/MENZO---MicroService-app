package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.*;
import com.menzo.Product_Service.Entity.ProductCategory;
import com.menzo.Product_Service.Exception.DuplicateCategoryException;
import com.menzo.Product_Service.Repository.CategoriesRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoriesService {

    private static final Logger log = LoggerFactory.getLogger(CategoriesService.class);

    @Autowired
    CategoriesRepo categoriesRepo;


//    Parent categories

    //    Get all parent categories
    public List<ParentCategoryDto> getAll() {
        List<ProductCategory> categoriesList = categoriesRepo.findAllParent();
        log.info("Fetched {} parent categories", categoriesList.size());
        List<ParentCategoryDto> parentCategoriesList = new ArrayList<>();
        for (ProductCategory p : categoriesList) {
            ParentCategoryDto parentCategory = new ParentCategoryDto(p.getId(), p.getCategoryName(), p.getIsActive(), p.getCreatedAt());
            parentCategoriesList.add(parentCategory);
        }
        return parentCategoriesList;
    }

    //    Get all parent with sub-categories list
    public List<NestedCategoryDto> getAllParentWithSub(){
        List<Object[]> results = categoriesRepo.findAllParentWithSub();
        Map<Long, NestedCategoryDto> parentMap = new HashMap<>();
        for (Object[] result: results){
            Long parentId = (Long) result[0];
            String parentName = (String) result[1];
            Long subId = (Long) result[2];
            String subName = (String) result[3];

            // Create or get the parent category DTO
            NestedCategoryDto parentCategory = parentMap.computeIfAbsent(parentId, id -> new NestedCategoryDto(id, parentName, new ArrayList<>()));

            // If a subcategory exists, add it to the parent's subcategories
            if (subId != null) {
                parentCategory.getSubCategories().add(new NestedCategoryDto(subId, subName, new ArrayList<>()));
            }
        }

        // Convert the map to a list
        List<NestedCategoryDto> categoriesList = new ArrayList<>(parentMap.values());
        return categoriesList;
//        // Print out the categories
//        for (NestedCategoryDto parent: categoriesList) {
//            System.out.println("Parent: " + parent.getCategoryName());
//            for (NestedCategoryDto sub: parent.getSubCategories()) {
//                System.out.println("Sub: " + sub.getCategoryName());
//            }
//        }
    }

    //    Get parent category by id
    public ParentCategoryDto getParentCategoryById(Long parentCategoryId) {
        ProductCategory parentCategory = categoriesRepo.findParentById(parentCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentCategoryId));
        log.info("Found parent category with ID: {}", parentCategoryId);
        return new ParentCategoryDto(parentCategory.getId(), parentCategory.getCategoryName(), parentCategory.getIsActive(), parentCategory.getCreatedAt());
    }

    //    Add new parent category
    public ProductCategory addNewParent(CreateParentCategoryDto newParentCategory) {
        if (categoriesRepo.existsByCategoryName(newParentCategory.getCategoryName())) {
            log.error("Parent category '{}' already exists", newParentCategory.getCategoryName());
            throw new DuplicateCategoryException("Category already exists.");
        }
        ProductCategory newProductCategory = new ProductCategory(newParentCategory.getCategoryName());
        newProductCategory.setParentCategoryId(null);
        newProductCategory.setIsActive(true);
        log.info("Saving new parent category: {}", newParentCategory.getCategoryName());
        return categoriesRepo.save(newProductCategory);
    }

    //    Update parent category by id
    public ProductCategory updateParentCategory(Long parentCategoryId, ParentCategoryDto latestParentCategory) {
        ProductCategory parentCategory = categoriesRepo.findParentById(parentCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found with ID: " + parentCategoryId));
        parentCategory.setCategoryName(latestParentCategory.getCategoryName() != null && !latestParentCategory.getCategoryName().isEmpty() ? latestParentCategory.getCategoryName() : parentCategory.getCategoryName());
        parentCategory.setIsActive(latestParentCategory.getIsActive() != null ? latestParentCategory.getIsActive() : parentCategory.getIsActive());
        log.info("Updated parent category with ID: {}", parentCategoryId);
        return categoriesRepo.save(parentCategory);
    }

    //    Delete parent category by id
    public boolean deleteParentCategory(Long parentCategoryId) {
        boolean parentCategoryExists = categoriesRepo.existsById(parentCategoryId);
        if (!parentCategoryExists) {
            log.error("Parent category not found with ID {}", parentCategoryId);
            throw new EntityNotFoundException("Parent category not found with ID: " + parentCategoryId);
        }
        log.info("Deleting parent category with ID {}", parentCategoryId);
        categoriesRepo.deleteParentById(parentCategoryId);
        boolean exists = categoriesRepo.existsById(parentCategoryId);
        if (!exists) {
            log.info("Parent category with ID {} successfully deleted", parentCategoryId);
            return true;
        } else {
            log.error("Parent category with ID {} could not be deleted", parentCategoryId);
            return false;
        }
    }

//    Sub categories

    //    Get all sub category by parent id
    public List<SubCategoryDto> getAllSubOfParentId(Long parentId) {
        if (!categoriesRepo.existsById(parentId)) {
            log.error("Parent category not found with ID: {}", parentId);
            throw new EntityNotFoundException("Parent category not found with ID: " + parentId);
        }
        List<ProductCategory> categoriesList = categoriesRepo.findAllSubByParentId(parentId);
        log.info("Fetched {} sub-categories with parent ID {}", categoriesList.size(), parentId);
        List<SubCategoryDto> subCategoriesList = new ArrayList<>();
        for (ProductCategory p : categoriesList) {
            SubCategoryDto subCategory = new SubCategoryDto(p.getId(), p.getParentCategoryId(), p.getCategoryName(), p.getIsActive(), p.getCreatedAt());
            subCategoriesList.add(subCategory);
        }
        return subCategoriesList;
    }

    //    Get sub category by id
    public SubCategoryDto getSubCategoryById(Long subCategoryId) {
        ProductCategory subCategory = categoriesRepo.findSubById(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Sub category not found with ID: " + subCategoryId));
        log.info("Found subcategory with ID: {}", subCategoryId);
        return new SubCategoryDto(subCategory.getId(), subCategory.getParentCategoryId(), subCategory.getCategoryName(), subCategory.getIsActive(), subCategory.getCreatedAt());
    }

    //    Add new sub category
    public ProductCategory addNewSub(CreateSubCategoryDto newSubCategory) {
        if (categoriesRepo.existsByCategoryNameAndParentCategoryId(newSubCategory.getCategoryName(), newSubCategory.getParentCategoryId())) {
            log.error("Sub-category '{}' already exists under parent Id {}", newSubCategory.getCategoryName(), newSubCategory.getParentCategoryId());
            throw new DuplicateCategoryException("Category already exists under this parent.");
        }
        ProductCategory newProductCategory = new ProductCategory(newSubCategory.getParentCategoryId(), newSubCategory.getCategoryName());
        newProductCategory.setIsActive(true);
        log.info("Saving new sub-category under parent ID {}: {}", newSubCategory.getParentCategoryId(), newSubCategory.getCategoryName());
        return categoriesRepo.save(newProductCategory);
    }

    //    Update sub category by id
    public ProductCategory updateSubCategory(Long subCategoryId, SubCategoryDto latestSubCategory) {
        ProductCategory subCategory = categoriesRepo.findSubById(subCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Sub category not found with ID: " + subCategoryId));
        subCategory.setParentCategoryId(latestSubCategory.getParentCategoryId() != null && latestSubCategory.getParentCategoryId() > 0 ? latestSubCategory.getParentCategoryId() : subCategory.getParentCategoryId());
        subCategory.setCategoryName(latestSubCategory.getCategoryName() != null && !latestSubCategory.getCategoryName().isEmpty() ? latestSubCategory.getCategoryName() : subCategory.getCategoryName());
        subCategory.setIsActive(latestSubCategory.getIsActive() != null ? latestSubCategory.getIsActive() : subCategory.getIsActive());
        log.info("Updated sub category with ID: {}", subCategoryId);
        return categoriesRepo.save(subCategory);
    }

    //    Delete sub category by id
    public boolean deleteSubCategory(Long subCategoryId) {
        boolean subCategoryExists = categoriesRepo.existsById(subCategoryId);
        if (!subCategoryExists) {
            log.error("Sub-category not found with ID {}", subCategoryId);
            throw new EntityNotFoundException("Sub category not found with ID: " + subCategoryId);
        }
        log.info("Deleting sub-category with ID {}", subCategoryId);
        categoriesRepo.deleteSubById(subCategoryId);
        boolean exists = categoriesRepo.existsById(subCategoryId);
        if (!exists) {
            log.info("Sub-category with ID {} successfully deleted", subCategoryId);
            return true;
        } else {
            log.error("Sub-category with ID {} could not be deleted", subCategoryId);
            return false;
        }
    }

}
