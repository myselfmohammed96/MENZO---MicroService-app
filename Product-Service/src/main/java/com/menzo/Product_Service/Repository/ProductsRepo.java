package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductsRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    public boolean existsByProductName(String productName);
}










//    public List<Product> findByProductNameContainingIgnoreCase(String productName);

//    *** Backup - D0 NOT TOUCH ***

//    @Query(value = "SELECT p.id AS id, p.product_name AS productName, c.category_name AS subCategoryName, " +
//            "MIN(i.price) AS startingPrice, SUM(i.qty_in_stock) AS totalQty, " +
//            "REPLACE(im.image_url, '\\\\', '/') AS iconImage, " +
//            "CASE WHEN SUM(i.is_active = 1) = COUNT(*) THEN 'ACTIVE' " +
//            "WHEN SUM(i.is_active = 0) = COUNT(*) THEN 'INACTIVE' " +
//            "ELSE 'PARTIALLY_ACTIVE' END AS activeStatus " +
//            "FROM products p " +
//            "JOIN product_items i ON p.id = i.product_id " +
//            "JOIN product_categories c ON p.category_id = c.id " +
//            "JOIN (SELECT product_item_id, image_url FROM product_images WHERE id IN " +
//            "(SELECT MIN(id) FROM product_images GROUP BY product_item_id)) im ON i.id = im.product_item_id " +
//            "GROUP BY p.id, p.product_name, c.category_name, im.image_url",
//            countQuery = "SELECT COUNT(DISTINCT p.id) FROM products p " +
//                    "JOIN product_items i ON p.id = i.product_id " +
//                    "JOIN product_categories c ON p.category_id = c.id " +
//                    "JOIN (SELECT product_item_id, image_url FROM product_images WHERE id IN " +
//                    "(SELECT MIN(id) FROM product_images GROUP BY product_item_id)) im ON i.id = im.product_item_id " +
//                    "GROUP BY p.id, p.product_name, c.category_name, im.image_url",
//            nativeQuery = true)
//    public Page<ProductListingView> findAllProductListing(Pageable pageable);

