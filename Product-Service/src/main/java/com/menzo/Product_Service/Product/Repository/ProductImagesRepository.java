package com.menzo.Product_Service.Product.Repository;

import com.menzo.Product_Service.Product.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductImagesRepository extends JpaRepository<ProductImage, UUID> {

    List<ProductImage> findBySuperSku(String superSku);


//    public List<ProductImage> findByProductId(UUID productId);

//    public List<ProductImage> findByProductItemId(UUID productItemId);

}
