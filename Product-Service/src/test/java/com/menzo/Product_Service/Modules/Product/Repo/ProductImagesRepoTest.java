package com.menzo.Product_Service.Modules.Product.Repo;

import com.menzo.Product_Service.Product.Entity.ProductImage;
import com.menzo.Product_Service.Product.Repository.ProductImagesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductImagesRepoTest {

    @Autowired
    private ProductImagesRepository productImagesRepo;

    @Test
    public void testFindBySuperSku() {
        List<ProductImage> imageUrls = productImagesRepo.findBySuperSku("CS-87-NBL");
        for (ProductImage image : imageUrls) {
            System.out.println(image.getImageUrl());
        }
    }

}