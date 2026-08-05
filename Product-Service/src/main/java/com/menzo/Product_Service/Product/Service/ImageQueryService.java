//package com.menzo.Product_Service.Product.Service;
//
//import com.menzo.Product_Service.Product.Entity.ProductImage;
//import com.menzo.Product_Service.Product.Repo.ProductImagesRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Service
//public class ImageQueryService {
//
//    private static final Logger logger = LoggerFactory.getLogger(ImageQueryService.class);
//
//    @Autowired
//    private ProductImagesRepository imagesRepo;
//
//
//    /*
//    *
//    *   Get product image entities by product image IDs
//    *
//     */
//    public Set<ProductImage> getImageEntitiesByIds(Set<Long> imageIds) {
//        List<ProductImage> images = imagesRepo.findAllById(imageIds);
//        return new HashSet<>(images);
//    }
//}
