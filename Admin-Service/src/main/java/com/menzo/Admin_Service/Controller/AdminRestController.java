package com.menzo.Admin_Service.Controller;

import com.menzo.Admin_Service.Dto.ProductListingDto;
import com.menzo.Admin_Service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    private ProductService productService;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "Hello from Admin.!";
    }

//    @GetMapping("/all-products-v2")
//    public Page<ProductListingDto> getAllProductsListingWithPagination(@RequestParam(defaultValue = "0") Integer page,
//                                                                       @RequestParam(defaultValue = "10") Integer size) {
//        Page<ProductListingDto> productsPage = productService.getAllProductsListingWithPagination(page, size);
//        return productsPage;
//    }
}
