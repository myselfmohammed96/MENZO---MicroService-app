package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.FilterDtos.FilterRequestDto;
import com.menzo.Product_Service.Dto.FilterDtos.QueryDetailsDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductListingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ProductsRepoCustomImplTest {

    @Autowired
    private ProductsRepoCustomImpl productsRepoCustom;

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
        Page<ProductListingDto> page = productsRepoCustom.findAdminProductListing(queryDetails);

        for(ProductListingDto dto : page.getContent()) {
            System.out.println(dto);
        }
        System.out.println(page.getContent().size());
    }

}