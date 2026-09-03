package com.menzo.Product_Service.Product.Repository;

import com.menzo.Product_Service.SearchAndFilter.Dto.QueryDetailsDto;
import com.menzo.Product_Service.Product.Dto.ProductDto.AdminProductListingDto;
import com.menzo.Product_Service.Product.Dto.ProductDto.UserProductListingDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductsRepositoryCustom {

    Page<AdminProductListingDto> findAdminProductListing(QueryDetailsDto queryDetails);

    Page<UserProductListingDto> findUserProductListing(QueryDetailsDto queryDetails);

    List<UUID> findProductsContaining(String[] keywords);

//    @Query(
//            nativeQuery = true,
//            value = """
//                    SELECT DISTINCT p.id
//                    FROM products p
//                    LEFT JOIN product_categories sc ON p.category_id = sc.id
//                    LEFT JOIN product_categories c ON sc.parent_category_id = c.id
//                    LEFT JOIN product_items i ON p.id = i.product_id
//                    LEFT JOIN product_configurations pc ON i.id = pc.product_item_id
//                    LEFT JOIN variation_options o ON pc.variation_option_id = o.id
//                    WHERE
//                        p.product_name LIKE CONCAT('%', :keyword, '%')
//                        OR p.generic_name LIKE CONCAT('%', :keyword, '%')
//                        OR sc.category_name LIKE CONCAT('%', :keyword, '%')
//                        OR c.category_name LIKE CONCAT('%', :keyword, '%')
//                        OR o.option_value LIKE CONCAT('%', :keyword, '%')
//                    """
//    )
//    List<Long> findProductsContaining(@Param("keyword") String keyword);

}
