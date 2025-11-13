package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Entity.Product;
import com.menzo.Product_Service.Repository.ProductsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductSpecificationServiceV2Test {

    @Autowired
    private ProductsRepo productsRepo;

//    @Test
//    public void testGetByIds() {
//        Specification<Product> byIds = ProductSpecificationServiceV2.getByIds(Arrays.asList(80L, 81L));
//        List<Product> all = productsRepo.findAll(byIds);
//        System.out.println(all);
//    }

}