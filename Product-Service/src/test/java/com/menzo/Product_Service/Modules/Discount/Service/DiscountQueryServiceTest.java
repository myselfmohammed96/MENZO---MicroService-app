package com.menzo.Product_Service.Modules.Discount.Service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class DiscountQueryServiceTest {

    @Autowired
    private DiscountQueryService queryService;

    @Test
    public void testGetDiscountListing() {
        Page<?> discountListing = queryService.getDiscountListing(0, 5, "", null);
        System.out.println("Page count: " + discountListing.getTotalPages());
        System.out.println("Page content: \n" + discountListing.getContent());
        System.out.println("Page: " + discountListing);
    }

}