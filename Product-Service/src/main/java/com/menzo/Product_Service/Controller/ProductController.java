package com.menzo.Product_Service.Controller;

import com.menzo.Product_Service.Dto.ProductDto.NewProductDto;
import com.menzo.Product_Service.Dto.ProductDto.NewProductItemDto;
import com.menzo.Product_Service.Dto.ProductDto.SizeDetailsDto;
import com.menzo.Product_Service.Entity.Product;
import com.menzo.Product_Service.Entity.ProductItem;
import com.menzo.Product_Service.Entity.VariationOption;
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



    @PostMapping("/add-product-v2")
    public String addProductV2(@ModelAttribute NewProductDto newProduct,
                               @RequestParam Map<String, String> variationMap,
                               @RequestParam("images") List<MultipartFile> images) throws IOException {
        if(images.size() > 9) throw new IllegalArgumentException("You can upload a maximum of 9 images.");
        System.out.println(newProduct);
        System.out.println(newProduct.getSizeStockMap());


        System.out.println(variationMap);

        Product product = productsService.addNewProduct(newProduct, variationMap, images);
        if (product == null) {
            throw new RuntimeException("Product adding failed");
        }

//        List<VariationOption> list = productsService.processVariations(variationMap, null);
//        System.out.println(list);
//        String redirectUrl = UriComponentsBuilder
//                .fromUriString("http://localhost:8080")
//                .pathSegment("admin", "all-products")
//                .toUriString();
//        return "redirect:" + redirectUrl;
        return "redirect:http://localhost:8080/index";
    }


//    @PostMapping("/add-product-item")
//    public String addProductItem(@ModelAttribute NewProductItemDto newProductItem,
//                                 @RequestParam Map<String, String> variationMap,
//                                 @RequestParam("images") List<MultipartFile> images) throws IOException {
//        if (images.size() > 9) {
//            throw new IllegalArgumentException("You can upload a maximum of 9 images.");
//        }
////        newProductItem.display();
//        ProductItem savedItem = productsService.addNewProductItem(
//                newProductItem,
//                variationMap,
//                images
//        );
//
//        String redirectUrl = UriComponentsBuilder
//                .fromUriString("http://localhost:8080")
//                .pathSegment("admin", "all-products")
//                .toUriString();
//        return "redirect:" + redirectUrl;
//    }

    @PostMapping("/add-item")
    public String addProductItem(@RequestPart("newItem") NewProductItemDto newProductItem,
                                 @RequestPart("sizeDetails") List<SizeDetailsDto> sizeDetails,
                                 @RequestPart("images") List<MultipartFile> images) {
        if (images.size() > 9) {
            throw new IllegalArgumentException("You can upload a maximum of 9 images.");
        }

        System.out.println(newProductItem);
//        for (SizeDetailsDto sizes : newProductItem.getSizeDetails()) {
//            System.out.println(sizes);
//        }
        System.out.println(sizeDetails);
        return "redirect:http://localhost:8080/index";
    }

}
