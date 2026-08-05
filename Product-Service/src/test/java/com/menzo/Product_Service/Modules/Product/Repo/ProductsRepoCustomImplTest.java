package com.menzo.Product_Service.Modules.Product.Repo;

import com.menzo.Product_Service.SearchAndFilter.Dto.QueryDetailsDto;
import com.menzo.Product_Service.Product.Dto.AdminProductListingDto;
import com.menzo.Product_Service.Product.Dto.UserProductListingDto;
import com.menzo.Product_Service.Product.Repo.ProductsRepositoryCustomImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ProductsRepoCustomImplTest {

    @Autowired
    private ProductsRepositoryCustomImpl productsRepoCustom;

    @Test
    public void testFindAdminProductListing() {
        Map<String, Integer> statusFlags = new HashMap<>();
        statusFlags.put("isSubCategoryDeleted", 0);
        statusFlags.put("isCategoryDeleted", 0);
        statusFlags.put("isSubCategoryActive", 1);
        statusFlags.put("isCategoryActive", 1);
        QueryDetailsDto queryDetails = QueryDetailsDto.builder()
                .page(0)
                .size(10)
                .sortRequest(null)
                .allowInactiveProductItems(true)
                .filterValues(null)
                .statusFlags(statusFlags)
                .build();
        Page<AdminProductListingDto> page = productsRepoCustom.findAdminProductListing(queryDetails);

        for(AdminProductListingDto dto : page.getContent()) {
            System.out.println(dto);
        }
        System.out.println(page.getContent().size());
    }

    @Test
    public void testFindUserProductListing() {
        Map<String, Integer> statusFlags = new HashMap<>();
        statusFlags.put("isItemActive", 1);
        statusFlags.put("podAvailable", 1);

        statusFlags.put("isCategoryDeleted", 0);
        statusFlags.put("isSubCategoryDeleted", 0);
        statusFlags.put("isCategoryActive", 1);
        statusFlags.put("isSubCategoryActive", 1);

        QueryDetailsDto queryDetails = QueryDetailsDto.builder()
                .page(0)
                .size(10)
                .sortRequest(null)
                .allowInactiveProductItems(true)
                .filterValues(null)
                .statusFlags(statusFlags)
                .build();
        Page<UserProductListingDto> page = productsRepoCustom.findUserProductListing(queryDetails);

        System.out.println("User Product Listing: ");
        for(UserProductListingDto dto : page.getContent()) {
            System.out.println(dto);
        }
        System.out.println(page.getContent().size());
    }

}