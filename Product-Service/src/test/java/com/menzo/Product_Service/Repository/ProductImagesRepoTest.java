package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Modules.Product.Entity.ProductImage;
import com.menzo.Product_Service.Modules.Product.Repo.ProductImagesRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductImagesRepoTest {

    @Autowired
    private ProductImagesRepo productImagesRepo;

    @Test
    public void testFindBySuperSku() {
        List<ProductImage> imageUrls = productImagesRepo.findBySuperSku("CS-87-NBL");
        for (ProductImage image : imageUrls) {
            System.out.println(image.getImageUrl());
        }
    }

}