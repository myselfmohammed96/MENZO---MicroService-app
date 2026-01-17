package com.menzo.Product_Service.Modules.Product.Repo;

import com.menzo.Product_Service.Modules.Product.Dto.ProductListingView;
import com.menzo.Product_Service.Modules.Product.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductsRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>, ProductsRepoCustom {

    public boolean existsByProductName(String productName);

    @Query(value = "SELECT DISTINCT p FROM Product p JOIN p.items i")
    public List<Product> findAllWithItems();

    @Query(
            value = """
                    SELECT p.id AS productId, p.product_name AS productName, 
                    p.category_id AS subCategoryId, p.pod_available AS podAvailable, 
                    p.created_at AS productCreatedAt, i.id AS itemId, 
                    i.super_sku AS itemSuperSku, i.price AS itemPrice, 
                    i.qty_in_stock AS itemStock, i.sku AS itemSku, 
                    i.is_active AS itemActive 
                    FROM products p 
                    join product_items i 
                    ON p.id = i.product_id
                    """,
            nativeQuery = true
    )
    public List<ProductListingView> findAllProductsWithSorting();



    //  get admin products listing
    @Query(
            value = """
                    SELECT\s
                    	p.id AS productId,\s
                    	p.product_name AS productName,\s
                    	c.subCategoryName,\s
                     	c.categoryName,\s
                     	it.minPrice,\s
                     	it.maxPrice,\s
                     	it.minStockQty,\s
                     	it.maxStockQty,\s
                     	it.latestCreatedAt,\s
                     	it.oldestCreatedAt,\s
                     	it.activeStatus\s
                    FROM products p\s
                    LEFT JOIN\s
                    	(SELECT
                    			sc.id AS subCategoryId,\s
                    			sc.category_name AS subCategoryName,\s
                    			c.id AS categoryId,\s
                    			c.category_name AS categoryName\s
                    		FROM product_categories sc\s
                    		INNER JOIN product_categories c\s
                    		ON sc.parent_category_id = c.id\s
                    		WHERE sc.is_deleted = 0\s
                    		AND c.is_deleted = 0\s
                    		AND sc.is_active = 1\s
                    		AND c.is_active = 1\s
                    	) AS c\s
                    ON p.category_id = c.subCategoryId\s
                    LEFT JOIN\s
                    	(SELECT\s
                    			i.product_id AS productId,\s
                    			MIN(i.price) AS minPrice,\s
                    			MAX(i.price) AS maxPrice,\s
                    			MIN(i.qty_in_stock) AS minStockQty,\s
                    			MAX(i.qty_in_stock) AS maxStockQty,\s
                    			MIN(i.created_at) AS oldestCreatedAt,
                    			MAX(i.created_at) AS latestCreatedAt,\s
                    			case\s
                    				when MIN(i.is_active) = 1 AND MAX(i.is_active) = 1 then 'active'\s
                    				when MIN(i.is_active) = 0 AND MAX(i.is_active) = 0 then 'inactive'\s
                    				ELSE 'partial'\s
                    			END AS activeStatus\s
                    		FROM product_items i\s
                    		GROUP BY i.product_id\s
                    	) AS it\s
                    ON p.id = it.productId
                    """,
            nativeQuery = true
    )
    public List<ProductListingView> findAdminProductsListing();


    //  returns list of available entities with provided idList
    List<Product> findByIdIn(List<Long> selectionList);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByProductNameContainingIgnoreCaseOrGenericNameContainingIgnoreCase(
            String productNameKeyword,
            String genericNameKeyword
    );
}










//    public List<Product> findByProductNameContainingIgnoreCase(String productName);

//    *** Backup - D0 NOT TOUCH ***

//    @Query(value = """
//            """SELECT
//                    p.id AS id,
//                    p.product_name AS productName,
//                    c.category_name AS subCategoryName,
//                    MIN(i.price) AS startingPrice,
//                    SUM(i.qty_in_stock) AS totalQty,
//                    REPLACE(im.image_url, '\\\\', '/') AS iconImage,
//                    CASE WHEN SUM(i.is_active = 1) = COUNT(*) THEN 'ACTIVE'
//                        WHEN SUM(i.is_active = 0) = COUNT(*) THEN 'INACTIVE'
//                        ELSE 'PARTIALLY_ACTIVE' END AS activeStatus
//                FROM products p
//                JOIN product_items i ON p.id = i.product_id
//                JOIN product_categories c ON p.category_id = c.id
//                JOIN (SELECT product_item_id, image_url FROM product_images WHERE id IN
//            (SELECT MIN(id) FROM product_images GROUP BY product_item_id)) im ON i.id = im.product_item_id
//            GROUP BY p.id, p.product_name, c.category_name, im.image_url
//            countQuery = "SELECT COUNT(DISTINCT p.id) FROM products p
//                    JOIN product_items i ON p.id = i.product_id
//                    JOIN product_categories c ON p.category_id = c.id
//                    JOIN (SELECT product_item_id, image_url FROM product_images WHERE id IN
//                    (SELECT MIN(id) FROM product_images GROUP BY product_item_id)) im ON i.id = im.product_item_id
//                    GROUP BY p.id, p.product_name, c.category_name, im.image_url
//                    """
////            nativeQuery = true)
////    public Page<ProductListingView> findAllProductListing(Pageable pageable);

