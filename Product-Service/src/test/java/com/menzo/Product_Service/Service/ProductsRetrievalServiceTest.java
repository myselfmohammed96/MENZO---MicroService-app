package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.FilterDtos.RequestDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductListingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
class ProductsRetrievalServiceTest {

    @Autowired
    private ProductsRetrievalService productsRetrievalService;

    @Test
    public void testGetAdminAllProductListing() {
        Page<ProductListingDto> adminAllProductListing = productsRetrievalService.getAdminAllProductListing(0,
                10,
                null,
                new RequestDto());

       System.out.println(adminAllProductListing);
    }

}