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
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/index")
    public String indexPage(@CookieValue(value = "JWT", required = false) String token,
                            Model model) {
        if (token != null && jwtUtil.validateToken(token)) {
            String userEmail = jwtUtil.extractUserEmail(token);
            ClientSideUserDetailsDto user = userService.getUserDetailsForClientSide(userEmail);
            model.addAttribute("user", user);
            System.out.println("this one is running..." + user);
        } else {
            model.addAttribute("user", null);
            System.out.println("This 2 is running...");
        }
//        System.out.println("Index user details: " + token);
        return "IndexTemplates/index-page.html";
    }

    @GetMapping("/all-categories")
    public String allCategories(@CookieValue(value = "JWT", required = false) String token,
                                Model model) {
        if(token != null && jwtUtil.validateToken(token)) {
            String userEmail = jwtUtil.extractUserEmail(token);
            ClientSideUserDetailsDto user = userService.getUserDetailsForClientSide(userEmail);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }
        List<CategoryMinimalDto> allCategories = productService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "IndexTemplates/all-categories";
    }

    @GetMapping("/category/{categoryId}")
    public String categoryById(@CookieValue(value = "JWT", required = false) String token,
                               @PathVariable("categoryId") Long id,
                               Model model) {
        if(token != null && jwtUtil.validateToken(token)) {
            String userEmail = jwtUtil.extractUserEmail(token);
            ClientSideUserDetailsDto user = userService.getUserDetailsForClientSide(userEmail);
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }

        ParentCategoryDto parentCategory = productService.getParentCategoryById(id);
        List<CategoryMinimalDto> subCategories = productService.getAllSubCategoriesByCategoryId(id);

        model.addAttribute("category", parentCategory);
        model.addAttribute("subCategories", subCategories);
        return "IndexTemplates/category";
    }

    @GetMapping("/product-listing")
    public String productListing() {
        return "IndexTemplates/product-listing";
    }

}
