package com.menzo.Product_Service.Modules.Product.Service;

import com.menzo.Product_Service.Modules.Product.Repo.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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