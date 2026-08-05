package com.menzo.Product_Service.Product.Repo;

import com.menzo.Product_Service.Product.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImagesRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findBySuperSku(String superSku);


//    public List<ProductImage> findByProductId(Long productId);

//    public List<ProductImage> findByProductItemId(Long productItemId);
}
