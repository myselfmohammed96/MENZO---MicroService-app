package com.menzo.Product_Service.Modules.Discount.Repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DiscountRepoTest {

    @Autowired
    private DiscountRepo discountRepo;

    @Test
    public void testExistsByDiscountCode() {
        boolean exists = discountRepo.existsByDiscountCode("abcde");
        System.out.println("Discount code exists: " + exists);
    }

}