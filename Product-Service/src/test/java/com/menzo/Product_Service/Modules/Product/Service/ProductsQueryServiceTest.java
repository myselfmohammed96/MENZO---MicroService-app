package com.menzo.Product_Service.Modules.Product.Service;

import com.menzo.Product_Service.Product.Dto.ProductDto.AdminProductListingDto;
import com.menzo.Product_Service.SearchAndFilter.Dto.RequestDto;
import com.menzo.Product_Service.Product.Service.ProductQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
class ProductsQueryServiceTest {

    @Autowired
    private ProductQueryService productsQueryService;

    @Test
    public void testGetAdminProductListing() {
        Page<AdminProductListingDto> result = productsQueryService.getAdminProductListing(
                0,
                10,
                "White L shirt",
                "",
                new RequestDto()
        );
        System.out.println(result.getContent());
    }

}