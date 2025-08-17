package com.menzo.Product_Service.Controller;

import com.menzo.Product_Service.Dto.ProductDto.NewProductDto;
import com.menzo.Product_Service.Dto.ProductDto.NewProductItemDto;
import com.menzo.Product_Service.Entity.Product;
import com.menzo.Product_Service.Entity.ProductItem;
import com.menzo.Product_Service.Service.ProductsRetrievalService;
import com.menzo.Product_Service.Service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private ProductsRetrievalService productsRetrievalService;

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute NewProductDto newProduct,
                           @RequestParam("images") List<MultipartFile> images) throws IOException {
        System.out.println("Vanthurchi... new product");
        if (images.size() > 3) {
            throw new IllegalArgumentException("You can upload a maximum of 3 images.");
        }
        Product savedProduct = productsService.addNewProduct(newProduct, images);

        String redirectUrl = UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .pathSegment("admin", "all-products")
                .toUriString();
        return "redirect:" + redirectUrl;
    }

    @PostMapping("/add-product-item")
    public String addProductItem(@ModelAttribute NewProductItemDto newProductItem,
                                 @RequestParam Map<String, String> variationMap,
                                 @RequestParam("images") List<MultipartFile> images) throws IOException {
        if (images.size() > 9) {
            throw new IllegalArgumentException("You can upload a maximum of 9 images.");
        }
        newProductItem.display();
        ProductItem savedItem = productsService.addNewProductItem(newProductItem, variationMap, images);

        String redirectUrl = UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .pathSegment("admin", "all-products")
                .toUriString();
        return "redirect:" + redirectUrl;
    }

}
