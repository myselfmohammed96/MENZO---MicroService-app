package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.FilterDtos.FilterTypeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductFilterServiceTest {

    @Autowired
    private ProductFilterService filterService;

    @Test
    public void testGetPriceRanges() {
        List<String> priceRanges = filterService.getPriceRanges();
        priceRanges.stream()
                .forEach(opt -> System.out.println("[" + opt + "]"));
    }

    @Test
    public void testGetUserProductFilters() {
        List<String> userProductFilters = filterService.getUserProductFilters();
        userProductFilters.stream()
                .forEach(opt -> System.out.println("[" + opt + "]"));
    }

    @Test
    public void testGetAdminProductFilters() {
        List<String> adminProductFilters = filterService.getAdminProductFilters();
        adminProductFilters.stream()
                .forEach(opt -> System.out.println("[" + opt + "]"));
    }

    @Test
    public void testGetAdminFilters() {
        List<FilterTypeDto> filters = filterService.getAdminFilters("sub,121");
        for(FilterTypeDto f : filters) {
            System.out.println(f);
            System.out.println(f.getFilterOptions().size());
        }
        System.out.println("Total no. of filters: " + filters.size());
    }

    @Test
    public void testGetStockFilters() {
        List<String> stockFilters = filterService.getStockFilters();
        stockFilters.stream()
                .forEach(opt -> System.out.println("[" + opt + "]"));
    }

}