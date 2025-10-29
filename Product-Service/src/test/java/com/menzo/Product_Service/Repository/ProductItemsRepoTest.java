package com.menzo.Product_Service.Repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductItemsRepoTest {

    @Autowired
    private ProductItemsRepo itemsRepo;

    @Test
    public void testGetNextItemId() {
        Long nextId = itemsRepo.getNextItemId();
        System.out.println(nextId);
    }

}