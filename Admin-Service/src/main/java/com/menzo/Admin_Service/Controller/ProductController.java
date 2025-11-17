package com.menzo.Admin_Service.Controller;

import com.menzo.Admin_Service.Dto.*;
import com.menzo.Admin_Service.Service.CategoriesService;
import com.menzo.Admin_Service.Service.ProductRetrievalService;
import com.menzo.Admin_Service.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelExtensionsKt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRetrievalService productRetrievalService;

    @Autowired
    private CategoriesService categoriesService;

    //  ********* Categories & Variations *********
    //  Categories - accordion page
    @GetMapping("/categories")
    public String adminCategoriesManagement(@RequestHeader("roles") String roles,
                                            Model model) {
        return "CategoriesAndVariations/categories-management";
    }

    //  Variations - accordion page
    @GetMapping("/variations")
    public String adminVariationsManagement(@RequestHeader("roles") String roles,
                                            Model model) {
        return "CategoriesAndVariations/variations-management";
    }



    //  ********* Add forms *********
    //  Product - add form                          *** add-product-form ***
//    @GetMapping("/add-product")
//    public String adminAddProductForm() {
////        model.addAttribute("newProduct", new NewProductDto());
//        return "Products/add-product-form";
//    }

    @GetMapping("/add-product-v2")
    public String addProductFormV2(@RequestHeader("roles") String roles,
                                   Model model) {
        NestedVariationDto sizesDto = productRetrievalService.getSizes();
        NestedVariationDto colorsDto = productRetrievalService.getColors();

        NewProductDto productDto = NewProductDto.builder()
                .status("active")
                .pod("available")
                .build();
        model.addAttribute("sizesList", sizesDto.getOptions());
        model.addAttribute("colorsList", colorsDto.getOptions());
        model.addAttribute("newProduct", productDto);
        return "Products/add-product-formV2";
    }

    //  Product item - add form
//    @GetMapping("/add-item")
//    public String adminAddProductItemForm() {
//////                                          @RequestParam("category-id") Long subCategoryId,
////                                          @RequestParam("id") Long productId,
////                                          Model model) {
//////        categoriesService.getSubCategoryByProductId(productId);
////        ProductMinimalDto productDto = productRetrievalService.getProductById(productId);
////        model.addAttribute("product", productDto);
////        model.addAttribute("newProductItem", new NewProductItemDto(productDto.getProductId()));
//        return "Products/add-product-item-form";
//    }



    //  ********* Listing pages *********
    //  Products listing
    @GetMapping("/all-products")
    public String getAllProductsListing() {
        return "Products/all-products";
    }

    @GetMapping("/product")
    public String getProductDetails(@RequestParam("id") Long productId,
                                    Model model) {
        model.addAttribute("productId", productId);
        return "Products/product-details-v2";
    }


    //  Product details & Product items listing - by product ID
//    @GetMapping( "/product-items")
//    public String getAllProductItemsByProductId() {
////        ProductDetailsDto productDetails = productRetrievalService.getProductDetailsById(productId);
////        model.addAttribute("productDetails", productDetails);@RequestParam("id") Long productId,
////                                                Model model
//        return "Products/all-product-items";
//    }


    //  ********* Details page *********
    //  Product item details - by product item ID
    @GetMapping("/product-details")
    public String getProductDetailsByItemId() {
        return "Products/product-details";
    }

//    public String getAllProductsListingWithPagination(@RequestParam(defaultValue = "0") Integer page,
//                                                      @RequestParam(defaultValue = "10") Integer size,
//                                                      Model model) {
//        Page<ProductListingDto> productsPage = productService.getAllProductsListingWithPagination(page, size);
//        model.addAttribute("productList", productsPage.getContent());
//        model.addAttribute("currentPage", page + 1);
//        model.addAttribute("totalPages", productsPage.getTotalPages());
//        return "Products/all-products";
//    }
}
