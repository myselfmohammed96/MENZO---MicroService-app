package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.CategoriesDto.ParentCategoryView;
import com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto;
import com.menzo.Product_Service.Entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepo extends JpaRepository<ProductCategory, Long> {

//    Parent categories
//    GET methods

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

    @Query(nativeQuery = true, value = "SELECT parent.id AS parentId, parent.category_name AS parentName, " +
            "child.id AS subId, child.category_name AS subName " +
            "FROM product_categories parent " +
            "LEFT JOIN product_categories child ON child.parent_category_id = parent.id " +
            "WHERE parent.parent_category_id IS NULL AND parent.id = :parentCategoryId " +
            "ORDER BY parent.id, child.id")
    public List<Object[]> findParentByIdWithSub(@Param("parentCategoryId") Long parentCategoryId);

//    DELETE, Exists methods

    @Query(nativeQuery = true, value = "DELETE FROM product_categories WHERE parent_category_id IS NULL AND id = :parentCategoryId")
    public void deleteParentById(@Param("parentCategoryId") Long parentCategoryId);

    public boolean existsByCategoryName(String categoryName);

    public boolean existsById(Long parentId);

//    Sub categories
//    GET methods

    @Query("SELECT new com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto(pc.id, pc.parentCategoryId, pc.categoryName, " +
            "pc.isActive, pc.createdAt) FROM ProductCategory pc WHERE pc.parentCategoryId = :parentId")
    public List<SubCategoryDto> findAllSubByParentId(@Param("parentId") Long parentId);

    @Query("SELECT new com.menzo.Product_Service.Dto.CategoriesDto.SubCategoryDto(pc.id, pc.parentCategoryId, pc.categoryName, " +
            "pc.isActive, pc.createdAt) FROM ProductCategory pc WHERE pc.parentCategoryId IS NOT NULL AND pc.id = :subCategoryId")
    public SubCategoryDto findSubByIdWithoutVariation(@Param("subCategoryId") Long subCategoryId);

    @Query(nativeQuery = true, value = "SELECT * FROM product_categories pc WHERE pc.parent_category_id IS NOT NULL AND pc.id = :subCategoryId")
    public Optional<ProductCategory> findSubById(@Param("subCategoryId") Long subCategoryId);

//    DELETE, Exists methods

    @Query(nativeQuery = true, value = "DELETE FROM product_categories WHERE parent_category_id IS NOT NULL AND id = :subCategoryId")
    public void deleteSubById(@Param("subCategoryId") Long subCategoryId);

    public boolean existsByCategoryNameAndParentCategoryId(String categoryName, Long parentCategoryId);

    @Query(value = "SELECT c1.id AS id, c1.category_name AS categoryName " +
            "FROM product_categories c1 " +
            "WHERE c1.is_active = 1 AND id = (SELECT c2.parent_category_id " +
            "FROM product_categories c2 " +
            "WHERE c2.is_active = 1 AND c2.parent_category_id IS NOT NULL " +
            "AND c2.id = :subCategoryId)",
            nativeQuery = true)
    public ParentCategoryView findCategoryBySubId(Long subCategoryId);

//    @Query(nativeQuery = true, value = "SELECT pc.id, pc.parent_category_id, pc.category_name, pc.is_active, pc.created_at " +
//            "FROM product_categories pc WHERE pc.parent_category_id IS NOT NULL AND id = :subCategoryId")
//    public Optional<ProductCategory> findSubById(@Param("subCategoryId") Long subCategoryId);

}
