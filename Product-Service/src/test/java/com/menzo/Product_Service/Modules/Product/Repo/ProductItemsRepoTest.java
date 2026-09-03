package com.menzo.Product_Service.Modules.Product.Repo;

import com.menzo.Product_Service.Product.Entity.ProductItem;
import com.menzo.Product_Service.Product.Repository.ProductItemsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class ProductItemsRepoTest {

    @Autowired
    private ProductItemsRepository itemsRepo;

    @Test
    public void testExistsBySuperSku() {
        boolean exists = itemsRepo.existsBySuperSku("CS-79-BK");
        System.out.println("Super SKU exists: " + exists);
    }

    @Test
    @Transactional
    public void testFindBySuperSku() {
        List<ProductItem> items = itemsRepo.findAllBySuperSku("FS-84-DMN");
        items.stream().forEach(item -> System.out.println(item));
    }

//    @Test
//    public void testFindSizeByItemId() {
//        String size = itemsRepo.findSizeByItemId("Size", 154L);
//        System.out.println(size);
//    }



//    @Test
//    public void testGetNextItemId() {
//        Long nextId = itemsRepo.getNextItemId();
//        System.out.println(nextId);
//    }

}