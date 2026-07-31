package com.menzo.Product_Service.Modules.Discount.Repo;

import com.menzo.Product_Service.Discount.Repo.DiscountRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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