package com.menzo.Product_Service.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

@SpringBootTest
class ProductsServiceTest {

    @Autowired
    private ProductsService productsService;

    @Test
    public void testAddCountryOfOrigin() throws Exception {
        Method method = ProductsService.class.getDeclaredMethod(
                "addCountryOfOrigin",
                String.class
        );
        method.setAccessible(true);
        Long countryId = (Long) method.invoke(
                productsService,
                "India"
        );
        System.out.println("Country ID: " + countryId);
    }

    @Test
    public void testProcessVariations() throws Exception {
        Method method = ProductsService.class.getDeclaredMethod(
                "processVariations",
                String.class,
                StringBuilder.class
        );
        method.setAccessible(true);
    }

}