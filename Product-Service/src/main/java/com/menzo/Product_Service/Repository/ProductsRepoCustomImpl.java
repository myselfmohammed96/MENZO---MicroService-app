package com.menzo.Product_Service.Repository;

import com.menzo.Product_Service.Dto.FilterDtos.FilterRequestDto;
import com.menzo.Product_Service.Dto.FilterDtos.QueryDetailsDto;
import com.menzo.Product_Service.Dto.ProductDto.ProductListingDto;
import com.menzo.Product_Service.Dto.VariationsDto.VariationDto;
import com.menzo.Product_Service.Service.VariationsRetrievalService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductsRepoCustomImpl implements ProductsRepoCustom {

    private final EntityManager entityManager;

    @Autowired
    private VariationsRetrievalService variationsRetrievalService;


    @Override
    public List<ProductListingDto> findAdminProductListing(QueryDetailsDto queryDetails) {

        //  generating WHERE Clause for given filter values
        Map<String, String> predicatesMap = new HashMap<>();
        if (queryDetails.getFilterValues() != null && !queryDetails.getFilterValues().isEmpty()) {
            predicatesMap = generatePredicates(queryDetails.getFilterValues());
        }

        StringBuilder innerWhereClause = new StringBuilder();

        if (predicatesMap.containsKey("variationFilterQuery")) {
            innerWhereClause.append("WHERE (").append(predicatesMap.get("variationFilterQuery")).append(") ");
        }

        if (predicatesMap.containsKey("priceAndStockFilterQuery")) {
            if (innerWhereClause.length() > 0) {
                innerWhereClause.append("AND (").append(predicatesMap.get("priceAndStockFilterQuery")).append(")");
            } else {
                innerWhereClause.append("WHERE (").append(predicatesMap.get("priceAndStockFilterQuery")).append(")");
            }
        }

        String productItemActiveFilter = queryDetails.isAllowInactiveProductItems() ? "" : "HAVING MAX(i.is_active) = 1";

        // building base query
        String baseQuery = """
                SELECT 
                        p.id AS productId, 
                        p.product_name AS productName, 
                        sc.category_name AS subCategoryName, 
                        c.category_name AS categoryName, 
                        it.minPrice, 
                        it.maxPrice, 
                        it.minStockQty, 
                        it.maxStockQty, 
                        it.latestCreatedAt, 
                        it.oldestCreatedAt, 
                        it.activeStatus 
                    FROM products p 
                    INNER JOIN product_categories sc ON p.category_id = sc.id 
                    INNER JOIN product_categories c ON sc.parent_category_id = c.id 
                    LEFT JOIN (
                        SELECT 
                                i.product_id AS productId, 
                                MIN(i.price) AS minPrice, 
                                MAX(i.price) AS maxPrice, 
                                MIN(i.qty_in_stock) AS minStockQty, 
                                MAX(i.qty_in_stock) AS maxStockQty, 
                                MIN(i.created_at) AS oldestCreatedAt, 
                                MAX(i.created_at) AS latestCreatedAt, 
                                CASE 
                                    WHEN MIN(i.is_active) = 1 AND MAX(i.is_active) = 1 THEN 'active' 
                                    WHEN MIN(i.is_active) = 0 AND MAX(i.is_active) = 0 THEN 'inactive' 
                                    ELSE 'partial' 
                                END AS activeStatus 
                            FROM product_items i 
                            JOIN product_configurations pc ON i.id = pc.product_item_id 
                            JOIN variation_options o ON pc.variation_option_id = o.id 
                            JOIN variations v ON o.variation_id = v.id 
                """ + innerWhereClause +
                " GROUP BY i.product_id " + productItemActiveFilter + """
                ) AS it ON p.id = it.productId 
                WHERE sc.is_deleted = :isSubCategoryDeleted 
                    AND c.is_deleted = :isCategoryDeleted 
                    AND sc.is_active = :isSubCategoryActive 
                    AND c.is_active = :isCategoryActive 
                    AND minPrice IS NOT NULL 
                    AND maxPrice IS NOT NULL 
                    AND minStockQty IS NOT NULL 
                    AND maxStockQty IS NOT NULL 
                    AND latestCreatedAt IS NOT NULL 
                    AND oldestCreatedAt IS NOT NULL 
                    AND activeStatus IS NOT NULL 
                """;

        Query query = entityManager.createNativeQuery(baseQuery, "ProductListingDtoMapping");

        //  fixed params
        query.setParameter("isSubCategoryDeleted", queryDetails.getStatusFlags().get("isSubCategoryDeleted"));
        query.setParameter("isCategoryDeleted", queryDetails.getStatusFlags().get("isCategoryDeleted"));
        query.setParameter("isSubCategoryActive", queryDetails.getStatusFlags().get("isSubCategoryActive"));
        query.setParameter("isCategoryActive", queryDetails.getStatusFlags().get("isCategoryActive"));

        return query.getResultList();
    }



    private Map<String, String> generatePredicates(List<FilterRequestDto> filterValues) {

        Map<String, String> predicatesMap = new HashMap<>();
        List<String> variationNames = variationsRetrievalService.getAllVariations()
                .stream()
                .map(VariationDto::getVariationName)
                .toList();

        //  generating predicates for 'variations' filter values
        List<FilterRequestDto> variationFilters = filterValues.stream()
                .filter(f -> variationNames.contains(f.getFilterType()))
                .toList();
        String variationFilterQuery = generateVariationPredicates(variationFilters);
        if (!variationFilterQuery.isEmpty()) predicatesMap.put("variationFilterQuery", variationFilterQuery);

        //  generating predicates for 'price-range' & 'stock' filter values
        List<FilterRequestDto> priceAndStockFilters = filterValues.stream()
                .filter(f -> f.getFilterType().equalsIgnoreCase("price")
                        || f.getFilterType().equalsIgnoreCase("stock"))
                .toList();
        String priceAndStockFilterQuery = generatePriceAndStockPredicates(priceAndStockFilters);
        if (!priceAndStockFilterQuery.isEmpty()) predicatesMap.put("priceAndStockFilterQuery", priceAndStockFilterQuery);

        return predicatesMap;
    }

    private String generateVariationPredicates(List<FilterRequestDto> variationFilters) {
        return variationFilters.stream()
                .map(f -> {
                    List<String> correctedValues = Arrays.stream(f.getValues().split(", "))
                            .map(String::trim)
                            .map(value -> "'" + value.toLowerCase() + "'")
                            .toList();

                    String filterValue = String.join(", ", correctedValues);

                    return "(LOWER(v.variation_name) = '" + f.getFilterType().toLowerCase() +
                            "' AND LOWER(o.option_value) IN (" + filterValue + "))";
                })
                .collect(Collectors.joining(" OR "));
    }

    private String generatePriceAndStockPredicates(List<FilterRequestDto> priceAndStockFilters) {
        List<String> predicates = new ArrayList<>();

        //  handling price filters
        priceAndStockFilters.stream()
                .filter(f -> f.getFilterType().equalsIgnoreCase("price"))
                .forEach(f -> {
                    Arrays.stream(f.getValues().toLowerCase().split(",\\s*"))
                            .forEach(p -> {
                                String[] priceValues = p.split(" to ");
                                predicates.add("i.price BETWEEN " + priceValues[0].trim() + " AND " + priceValues[1].trim());
                            });
                });

        //  handling stock filters
        priceAndStockFilters.stream()
                .filter(f -> f.getFilterType().equalsIgnoreCase("stock"))
                .forEach(f -> {
                    Arrays.stream(f.getValues().toLowerCase().split(",\\s*"))
                            .forEach(s -> {
                                String[] stockValues = s.split(" to ");
                                predicates.add("i.qty_in_stock BETWEEN " + stockValues[0].trim() + " AND " + stockValues[1].trim());
                            });
                });

        return String.join(" OR ", predicates);
    }

}





//        String innerWhereClause = innerWhereClauseQueries.isEmpty() ? "" : innerWhereClauseQueries.toString();
//        String outerWhereClause = "";

//        StringBuilder queryBuilder = new StringBuilder(baseQuery);
//        Map<String, Object> params = new HashMap<>();

//        //  building filter query (Dynamic WHERE clause)
//        if (queryDetails.getFilterValues() != null && !queryDetails.getFilterValues().isEmpty()) {
//            queryBuilder.append("\nWHERE ");
//            List<String> conditions = new ArrayList<>();
//
//            int index = 0;
//            for (FilterRequestDto filter : queryDetails.getFilterValues()) {
//                String paramName = filter.getFilterType() + index;
//                conditions.add(filter.getFilterType() + " IN (:" + paramName + ")");
//                params.put(paramName, Arrays.asList(filter.getValues().split(",")));
//                index++;
//            }
//            queryBuilder.append(String.join(" AND ", conditions));
//        }

//        //  sorting query
//        if (queryDetails.getSortRequest() != null && !queryDetails.getSortRequest().isEmpty()) {
//            String[] sortParams = queryDetails.getSortRequest().toLowerCase().split(",");
//            if (sortParams.length == 2) {
//                queryBuilder.append("\nORDER BY ")
//                        .append(sortParams[0].trim())
//                        .append(" ")
//                        .append(sortParams[1].trim());
//            }
//        }

//        Query query = entityManager.createNativeQuery(queryBuilder.toString(), "ProductListingDtoMapping");









//  dynamic filter params
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            query.setParameter(entry.getKey(), entry.getValue());
//        }