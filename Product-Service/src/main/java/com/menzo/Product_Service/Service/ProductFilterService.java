package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Repository.VariationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductFilterService {

    @Autowired
    private VariationsRetrievalService variationsRetrievalService;

    public enum ProductFilterType {
        PRICE,
        COLORS,
        SIZE,
        FIT_TYPE;
    }

    private static final List<String> PRICE_RANGES = List.of(
            "0 to 499",
            "500 to 999",
            "1000 to 1499",
            "1500 to 2499",
            "2500 to 3499",
            "3500 and above"
    );

    public Map<ProductFilterType, List<String>> getGlobalFilters() {
        Map<ProductFilterType, List<String>> filters = new LinkedHashMap<>();

        filters.put(ProductFilterType.PRICE, PRICE_RANGES);
        filters.put(ProductFilterType.COLORS, variationsRetrievalService.getOptionsByVariationName(null, "Colors"));
        filters.put(ProductFilterType.SIZE, variationsRetrievalService.getOptionsByVariationName(null, "Size"));
        filters.put(ProductFilterType.FIT_TYPE, variationsRetrievalService.getOptionsByVariationName(null, "Fit type"));

        return filters;
    }

    public Map<ProductFilterType, List<String>> getCategoryFilters(Long categoryId) {
        Map<ProductFilterType, List<String>> filters = new LinkedHashMap<>();

        filters.put(ProductFilterType.PRICE, PRICE_RANGES);
        filters.put(ProductFilterType.COLORS, variationsRetrievalService.getOptionsByVariationName(categoryId, "Colors"));
        filters.put(ProductFilterType.SIZE, variationsRetrievalService.getOptionsByVariationName(categoryId, "Size"));
        filters.put(ProductFilterType.FIT_TYPE, variationsRetrievalService.getOptionsByVariationName(categoryId, "Fit type"));

        return filters;
    }
}
