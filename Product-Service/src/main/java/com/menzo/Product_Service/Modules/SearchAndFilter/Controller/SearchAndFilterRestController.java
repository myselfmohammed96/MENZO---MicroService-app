package com.menzo.Product_Service.Modules.SearchAndFilter.Controller;

import com.menzo.Product_Service.Modules.SearchAndFilter.Dto.FilterTypeDto;
import com.menzo.Product_Service.Product.Service.ProductFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search-filter")
public class SearchAndFilterRestController {

    @Autowired
    private ProductFilterService productFilterService;

    @GetMapping("/admin-filters")
    public List<FilterTypeDto> getAdminFilterOptions(@RequestParam(value = "type", required = false) String filterType) {
//        return productFilterService.getGlobalFilters();
        return productFilterService.getAdminFilters(filterType);
    }

    @GetMapping("/category")
    public Map<ProductFilterService.ProductFilterType, List<String>> getCategoryFilterOptions(@RequestParam("id") Long categoryId){
        return productFilterService.getCategoryFilters(categoryId);
    }
}
