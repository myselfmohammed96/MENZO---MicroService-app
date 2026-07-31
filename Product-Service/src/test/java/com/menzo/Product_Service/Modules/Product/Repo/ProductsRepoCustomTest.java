package com.menzo.Product_Service.Modules.Product.Repo;

import com.menzo.Product_Service.Product.Repo.ProductsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductsRepoCustomTest {

    @Autowired
    private ProductsRepo productsRepo;

    @Test
    public void testFindProductsContaining() {
        String[] keywords = {"white", "L", "shirt"};
        List<Long> result = productsRepo.findProductsContaining(keywords);
        System.out.println(result);
    }
}
