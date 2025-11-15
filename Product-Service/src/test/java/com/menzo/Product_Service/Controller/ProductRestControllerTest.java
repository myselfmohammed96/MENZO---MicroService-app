package com.menzo.Product_Service.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class ProductRestControllerTest {

    @Autowired
    private ProductRestController productRestController;

    @Test
    public void testGetAllProducts() {
        ResponseEntity<?> allProducts = productRestController.getAllProducts(
                0,
                15,
                null,
                null
        );
        System.out.println("Here -> " + allProducts.getBody());
    }

}