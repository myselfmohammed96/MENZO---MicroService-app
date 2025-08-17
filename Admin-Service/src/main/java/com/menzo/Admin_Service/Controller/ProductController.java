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
    public String adminCategoriesManagement(@RequestHeader("roles") String roles, Model model) {
        return "CategoriesAndVariations/categories-management";
    }

    //  Variations - accordion page
    @GetMapping("/variations")
    public String adminVariationsManagement(@RequestHeader("roles") String roles, Model model) {
        return "CategoriesAndVariations/variations-management";
    }

    //  ********* Add forms *********
    //  Product - add form
    @GetMapping("/add-product")
    public String adminAddProductForm(@RequestHeader("roles") String roles,
                                      Model model) {
        model.addAttribute("newProduct", new NewProductDto());
        return "Products/add-product-form";
    }

    //  Product item - add form
    @GetMapping("/add-item")
    public String adminAddProductItemForm(@RequestHeader("roles") String roles,
//                                          @RequestParam("category-id") Long subCategoryId,
                                          @RequestParam("id") Long productId,
                                          Model model) {
//        categoriesService.getSubCategoryByProductId(productId);
        ProductMinimalDto productDto = productRetrievalService.getProductById(productId);
        model.addAttribute("product", productDto);
        model.addAttribute("newProductItem", new NewProductItemDto(productDto.getProductId()));
        return "Products/add-product-item-form";
    }

    //  ********* Listing pages *********
    //  Products listing
    @GetMapping("/all-products")
    public String getAllProductsListing() {
        return "Products/all-products";
    }

    //  Product details & Product items listing - by product ID
    @GetMapping("/product-items")
    public String getAllProductItemsByProductId(@RequestParam("id") Long productId,
                                                Model model) {
        ProductDetailsDto productDetails = productRetrievalService.getProductDetailsById(productId);
        model.addAttribute("productDetails", productDetails);
        return "Products/all-product-items";
    }

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
