package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductsRepoTest {

    @Autowired
    private ProductsRepo productsRepo;

    @Test
    public void testFindAllWithItems() {
        List<Product> allItems = productsRepo.findAllWithItems();
        System.out.println(allItems);
    }

}