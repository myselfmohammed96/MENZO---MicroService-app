package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.NestedCategoryDto;
import com.menzo.Product_Service.Entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface CategoriesRepo extends JpaRepository<ProductCategory, Long> {

//    Parent categories

    @Query(nativeQuery = true, value = "SELECT * FROM product_categories WHERE parent_category_id IS NULL")
    public List<ProductCategory> findAllParent();

    @Query(nativeQuery = true, value = "SELECT parent.id AS parentId, parent.category_name AS parentName, " +
            "child.id AS subId, child.category_name AS subName " +
            "FROM product_categories parent " +
            "LEFT JOIN product_categories child ON child.parent_category_id = parent.id " +
            "WHERE parent.parent_category_id IS NULL " +
            "ORDER BY parent.id, child.id")
    public List<Object[]> findAllParentWithSub();

    @Query(nativeQuery = true, value = "SELECT * FROM product_categories pc WHERE pc.parent_category_id IS NULL AND id = :parentCategoryId")
    public Optional<ProductCategory> findParentById(@Param("parentCategoryId") Long parentCategoryId);

    @Query(nativeQuery = true, value = "DELETE FROM product_categories WHERE parent_category_id IS NULL AND id = :parentCategoryId")
    public void deleteParentById(@Param("parentCategoryId") Long parentCategoryId);

    public boolean existsByCategoryName(String categoryName);

    public boolean existsById(Long parentId);

//    Sub categories

    @Query(nativeQuery = true, value = "SELECT * FROM product_categories WHERE parent_category_id = :parentId")
    public List<ProductCategory> findAllSubByParentId(@Param("parentId") Long parentId);

    @Query(nativeQuery = true, value = "SELECT * FROM product_categories pc WHERE pc.parent_category_id IS NOT NULL AND id = :subCategoryId")
    public Optional<ProductCategory> findSubById(@Param("subCategoryId") Long subCategoryId);

    @Query(nativeQuery = true, value = "DELETE FROM product_categories WHERE parent_category_id IS NOT NULL AND id = :subCategoryId")
    public void deleteSubById(@Param("subCategoryId") Long subCategoryId);

    public boolean existsByCategoryNameAndParentCategoryId(String categoryName, Long parentCategoryId);


}
