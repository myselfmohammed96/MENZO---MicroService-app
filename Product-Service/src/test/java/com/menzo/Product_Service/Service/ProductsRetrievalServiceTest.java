package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.FilterDtos.RequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductsRetrievalServiceTest {

    @Autowired
    private ProductsRetrievalService productsRetrievalService;

    @Test
    public void testGetAdminAllProductListing() {
        productsRetrievalService.getAdminAllProductListing(0,
                10,
                "",
                new RequestDto());
    }

}