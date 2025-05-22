package com.menzo.Admin_Service.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ProductController {

    @GetMapping("/categories")
    public String adminCategoriesManagement(Model model) {
        return "CategoriesAndVariations/categories-management";
    }

    @GetMapping("/variations")
    public String adminVariationsManagement(Model model) {
        return "CategoriesAndVariations/variations-management";
    }
}
