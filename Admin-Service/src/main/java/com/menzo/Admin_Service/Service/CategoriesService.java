package com.menzo.Admin_Service.Service;

import com.menzo.Admin_Service.Feign.ProductServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriesService {

    @Autowired
    private ProductServiceFeign productServiceFeign;

//    public void getSubCategoryByProductId(Long productId) {
//        productServiceFeign.getSubCategoryByProductId(productId);
//    }
}
