package com.menzo.Product_Service.Product.Service;

import com.menzo.Product_Service.SearchAndFilter.Dto.FilterTypeDto;
import com.menzo.Product_Service.Variation.Dto.VariationOptionsDto;
import com.menzo.Product_Service.Enum.Components;
import com.menzo.Product_Service.Variation.Service.OptionQueryService;
import com.menzo.Product_Service.Variation.Service.VariationQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:metaConfig.properties")
public class ProductFilterService {

    @Autowired
    private VariationQueryService variationQueryService;

    @Autowired
    private OptionQueryService optionQueryService;

    private List<String> userProductFilters;

    private List<String> adminProductFilters;

    private static List<String> priceRanges;

    private static List<String> stockFilters;

    private static List<String> podAvailableFilters;



    //  getters & setters for external metaConfig file - TESTED

    public List<String> getUserProductFilters() {
        return userProductFilters;
    }

    @Value("#{'${product.filter.type.user}'.split(', ')}")
    public void setUserProductFilters(List<String> userProductFilters) {
        this.userProductFilters = userProductFilters;
    }

    public List<String> getAdminProductFilters() {
        return adminProductFilters;
    }

    @Value("#{'${product.filter.type.admin}'.split(', ')}")
    public void setAdminProductFilters(List<String> adminProductFilters) {
        this.adminProductFilters = adminProductFilters;
    }

    public List<String> getPriceRanges() {
        return this.priceRanges;
    }

    @Value("#{'${product.filter.options.price-range}'.split(', ')}")
    public void setPriceRanges(List<String> ranges) {
        this.priceRanges = ranges;
    }

    public List<String> getStockFilters() {
        return stockFilters;
    }

    @Value("#{'${product.filter.options.stock}'.split(', ')}")
    public void setStockFilters(List<String> stockFilters) {
        this.stockFilters = stockFilters;
    }

    public List<String> getPodAvailableFilters() {
        return podAvailableFilters;
    }

    @Value("#{'${product.filter.options.pod-available}'.split(', ')}")
    public void setPodAvailableFilters(List<String> podAvailableFilters) {
        this.podAvailableFilters = podAvailableFilters;
    }



    //  ********* filter providers *********

    public List<FilterTypeDto> getAdminFilters(String listingType) {

        List<FilterTypeDto> filters = adminProductFilters.stream()
                .map(type -> filterTypeProvider(type))
                .collect(Collectors.toList());

        if (listingType != null && !listingType.isEmpty()) {

            // listing type contains listing type (category or sub-category) with ID (eg): cat,1 or sub,121
            List<VariationOptionsDto> variations;
            String[] typeParams = listingType.split(",");
            if (typeParams[0].toLowerCase().equals("cat")) {
                variations = variationQueryService.getAllVariationsWithOptionsByCategory(
                        Components.CATEGORY,
                        UUID.fromString(typeParams[1])
                );
            } else if(typeParams[0].toLowerCase().equals("sub")) {
                variations = variationQueryService.getAllVariationsWithOptionsByCategory(
                        Components.SUB_CATEGORY,
                        UUID.fromString(typeParams[1])
                );
            } else {
                throw new IllegalArgumentException("Invalid listing type argument. Must be either 'cat' or 'sub': " + listingType);
            }
            List<FilterTypeDto> variationFilters = variations.stream().map(variation -> {
                List<String> options = variation.getOptions().stream()
                        .map(option -> option.getOptionValue())
                        .toList();
                return FilterTypeDto.builder()
                        .filterType(variation.getVariationName().toUpperCase())
                        .typeValue(variation.getVariationName().toLowerCase())        // ## typeValue can differ later
                        .filterOptions(options)
                        .build();
            }).toList();
            filters.addAll(variationFilters);
        }
        return filters;
    }



//    ********* Utility methods *********

    //  provide filter options respect to the given 'filter type'
    public FilterTypeDto filterTypeProvider(String filterType) {

        if (filterType == null || filterType.isEmpty()) throw new IllegalArgumentException("Invalid 'filterType': " + filterType);
        return switch (filterType) {
            case "price-range" -> {
                yield FilterTypeDto.builder()
                        .filterType(ProductFilterType.PRICE.toString())
                        .typeValue("price-range")
                        .filterOptions(priceRanges)
                        .build();
            }
            case "colors" -> {
                //  ## add hex code with the colors string
                yield FilterTypeDto.builder()
                        .filterType(ProductFilterType.COLORS.toString())
                        .typeValue("colors")
                        .filterOptions(optionQueryService.getOptionsByVariationName(null, "Colors"))
                        .build();
            }
            case "size" -> {
                yield FilterTypeDto.builder()
                        .filterType(ProductFilterType.SIZE.toString())
                        .typeValue("size")
                        .filterOptions(optionQueryService.getOptionsByVariationName(null, "Size"))
                        .build();
            }
            case "stock" -> {
                yield FilterTypeDto.builder()
                        .filterType(ProductFilterType.STOCK.toString())
                        .typeValue("stock")
                        .filterOptions(stockFilters)
                        .build();
            }
            case "pod-available" -> {
                yield FilterTypeDto.builder()
                        .filterType(ProductFilterType.PAY_ON_DELIVERY.toString())
                        .typeValue("pod-available")
                        .filterOptions(podAvailableFilters)
                        .build();
            }
//            case "createdAt" -> {}
//            case "active-status" -> {}
            default -> throw new IllegalStateException("Unexpected value: " + filterType);
        };
    }



    public Map<ProductFilterType, List<String>> getGlobalFilters() {
        Map<ProductFilterType, List<String>> filters = new LinkedHashMap<>();

        filters.put(ProductFilterType.PRICE, priceRanges);
        filters.put(ProductFilterType.COLORS, optionQueryService.getOptionsByVariationName(null, "Colors"));
        filters.put(ProductFilterType.SIZE, optionQueryService.getOptionsByVariationName(null, "Size"));
//        filters.put(ProductFilterType.FIT_TYPE, variationsRetrievalService.getOptionsByVariationName(null, "Fit type"));

        return filters;
    }

    public Map<ProductFilterType, List<String>> getCategoryFilters(Long categoryId) {
        Map<ProductFilterType, List<String>> filters = new LinkedHashMap<>();

        filters.put(ProductFilterType.PRICE, priceRanges);
        filters.put(ProductFilterType.COLORS, optionQueryService.getOptionsByVariationName(categoryId, "Colors"));
        filters.put(ProductFilterType.SIZE, optionQueryService.getOptionsByVariationName(categoryId, "Size"));
//        filters.put(ProductFilterType.FIT_TYPE, variationsRetrievalService.getOptionsByVariationName(categoryId, "Fit type"));

        return filters;
    }

    public enum ProductFilterType {
        PRICE,
        COLORS,
        SIZE,
        PAY_ON_DELIVERY,
        STOCK,
        STATUS;         //  activeStatus
    }
}
