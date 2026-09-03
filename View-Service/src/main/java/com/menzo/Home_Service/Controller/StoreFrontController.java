package com.menzo.Home_Service.Controller;

import com.menzo.Home_Service.Dto.CategoryMinimalDto;
import com.menzo.Home_Service.Dto.ClientSideUserDetailsDto;
import com.menzo.Home_Service.Dto.ParentCategoryDto;
import com.menzo.Home_Service.Service.ProductService;
import com.menzo.Home_Service.Service.UserService;
import com.menzo.Home_Service.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StoreFrontController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;


    /*
     *
     *   Get index/home page
     *
     */
    @GetMapping("/index")
    public String getIndexPage(@CookieValue(value = "JWT", required = false) String token,
                            Model model) {
        if (token != null && jwtUtil.validateToken(token)) {
            String userEmail = jwtUtil.extractUserEmail(token);
            ClientSideUserDetailsDto user = userService.getUserDetailsForClientSide(userEmail);
            model.addAttribute("user", user);
//            System.out.println("this one is running..." + user);
        } else {
            model.addAttribute("user", null);
//            System.out.println("This 2 is running...");
        }
//        System.out.println("Index user details: " + token);
        return "store-front-template/index-page.html";
    }


    /*
     *
     *   Get all categories page
     *
     */
    @GetMapping("/all-categories")
    public String getAllCategoriesPage(@CookieValue(value = "JWT", required = false) String token,
                                Model model) {
        if (token != null && jwtUtil.validateToken(token)) {
            String userEmail = jwtUtil.extractUserEmail(token);
            ClientSideUserDetailsDto user = userService.getUserDetailsForClientSide(userEmail);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }
        List<CategoryMinimalDto> allCategories = productService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "store-front-template/all-categories.html";
    }


    /*
     *
     *   Get category page
     *   Contains sub-categories of the given category ID
     *
     */
    @GetMapping("/category/{categoryId}")
    public String getSubCategoriesPageByCategoryId(@CookieValue(value = "JWT", required = false) String token,
                               @PathVariable("categoryId") Long id,
                               Model model) {
        if (token != null && jwtUtil.validateToken(token)) {
            String userEmail = jwtUtil.extractUserEmail(token);
            ClientSideUserDetailsDto user = userService.getUserDetailsForClientSide(userEmail);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        ParentCategoryDto parentCategory = productService.getParentCategoryById(id);
        List<CategoryMinimalDto> subCategories = productService.getAllSubCategoriesByCategoryId(id);
//
        model.addAttribute("category", parentCategory);
        model.addAttribute("subCategories", subCategories);
        return "store-front-template/category.html";
    }


    /*
     *
     *   Get product listing page
     *
     */
    @GetMapping("/product-listing")
    public String getProductListingPage(@RequestParam(value = "id", required = false) Long id,
                                 Model model) {
        System.out.println("id: " + id);
//        model.addAttribute()
        return "store-front-template/product-listing.html";
    }


    /*
     *
     *   Get product details page
     *
     */
    @GetMapping("/product")
    public String getProductDetailsPage(@RequestParam("ssku") String superSku,
                                 Model model) {
//        productService.getProductDetails(superSku);
        model.addAttribute("superSku", superSku);
        return "store-front-template/product-details.html";
    }


    /*
     *
     *   Get contact us page
     *
     */
    @GetMapping("/contact-us")
    public String getContactUsPage() {
        return "store-front-template/contact-us.html";
    }


    /*
     *
     *   Get about us page
     *
     */
    @GetMapping("/about-us")
    public String getAboutUsPage() {
        return "store-front-template/about-us.html";
    }


    /*
     *
     *   Get FAQ page
     *
     */
    @GetMapping("/faq")
    public String getFaqPage() {
        return "store-front-template/faq.html";
    }


    /*
     *
     *   Get privacy policy page
     *
     */
    @GetMapping("/privacy-policy")
    public String getPrivacyPolicyPage() {
        return "store-front-template/privacy-policy.html";
    }


    /*
     *
     *   Get terms and conditions page
     *
     */
    @GetMapping("/terms-and-conditions")
    public String getTermsAndConditionsPage() {
        return "store-front-template/terms-and-conditions.html";
    }

}
