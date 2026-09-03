package com.menzo.Product_Service.Product.Repository;

import com.menzo.Product_Service.SearchAndFilter.Dto.FilterRequestDto;
import com.menzo.Product_Service.SearchAndFilter.Dto.QueryDetailsDto;
import com.menzo.Product_Service.Product.Dto.ProductDto.AdminProductListingDto;
import com.menzo.Product_Service.Product.Dto.ProductDto.UserProductListingDto;
import com.menzo.Product_Service.Variation.Service.VariationQueryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductsRepositoryCustomImpl implements ProductsRepositoryCustom {

    private final EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(ProductsRepositoryCustomImpl.class);

    @Autowired
    private VariationQueryService variationQueryService;


    /*
     *  ---------------------------------------------
     *  ********* Search processing methods *********
     *  ---------------------------------------------
     */

    //  Search product IDs - for given keywords
    @Override
    public List<UUID> findProductsContaining(String[] keywords) {

        if (keywords == null || keywords.length == 0) {
            return Collections.emptyList();
        }

        String selectClause = "SELECT DISTINCT p.id FROM products p WHERE ";
        List<String> whereClauses = new ArrayList<>();

        //  building WHERE clauses dynamically - for each keyword
        for (int i = 0; i < keywords.length; i++) {
            String param = ":kw" + i;
            String clause = String.format("""
                    (p.product_name LIKE CONCAT('%%', %s, '%%')
                    OR p.generic_name LIKE CONCAT('%%', %s, '%%')
                    OR EXISTS (
                        SELECT 1
                        FROM product_items i
                        JOIN product_configurations pc ON i.id = pc.product_item_id
                        JOIN variation_options o ON pc.variation_option_id = o.id
                        WHERE i.product_id = p.id
                            AND o.option_value LIKE CONCAT('%%', %s, '%%')
                    )
                    OR EXISTS (
                        SELECT 1
                        FROM product_categories sc
                        LEFT JOIN product_categories c ON sc.parent_category_id = c.id
                        WHERE p.category_id = sc.id
                            AND (sc.category_name LIKE CONCAT('%%', %s, '%%')
                                OR c.category_name LIKE CONCAT('%%', %s, '%%'))
                    ))""", param, param, param, param, param);
            whereClauses.add(clause);
        }

        //  joining all clauses with 'AND'
        String queryString = selectClause + String.join(" AND ", whereClauses);

        Query query = entityManager.createNativeQuery(queryString);

        for (int i = 0; i < keywords.length; i++) {
            query.setParameter("kw" + i, keywords[i]);
        }

        List<Object> resultRows = query.getResultList();

        List<UUID> productIds = resultRows.stream()
                .map(r -> (UUID) r)
                .toList();

        return productIds;
    }


    /*
     *  -------------------------------------------
     *  ********* Product Listing methods *********
     *  -------------------------------------------
     */

    //  ADMIN product listing
    @Override
    public Page<AdminProductListingDto> findAdminProductListing(QueryDetailsDto queryDetails) {

        //  SELECT clause
        String adminSelectClause = """
                SELECT 
                        p.id AS productId, 
                        p.product_name AS productName, 
                        sc.category_name AS subCategoryName, 
                        c.category_name AS categoryName, 
                        it.minMrp, 
                        it.maxMrp, 
                        it.minPrice, 
                        it.maxPrice, 
                        it.minStockQty, 
                        it.maxStockQty, 
                        it.latestCreatedAt, 
                        it.oldestCreatedAt, 
                        it.colorCount, 
                        it.activeStatus 
                """;

        //  Predicates for filter values (price, quantity & variation filters)
        Map<String, String> predicatesMap = new HashMap<>();
        if (queryDetails.getFilterValues() != null && !queryDetails.getFilterValues().isEmpty()) {
            logger.info("generating admin filter predicates");
            predicatesMap = generatePredicates(queryDetails.getFilterValues());
        }

        //  WHERE clause for product IDs (search results)
        String searchResultProductIds = queryDetails.getSearchResultProductIds() != null
                && !queryDetails.getSearchResultProductIds().isEmpty()
                ? productIdsPredicate(queryDetails.getSearchResultProductIds())
                : "";

        //  WHERE clause for category & sub-category filters
        String categoryFilters = "";
        if (queryDetails.getCategoryName() != null && !queryDetails.getCategoryName().isEmpty()) {
            categoryFilters += "c.category_name = '" + queryDetails.getCategoryName().toLowerCase() + "' AND ";
        }
        if (queryDetails.getSubCategoryNames() != null && !queryDetails.getSubCategoryNames().isEmpty()) {
            categoryFilters += "sc.category_name IN ('" + String.join("', '", queryDetails.getSubCategoryNames()) + "') AND";
        }

        //  WHERE clause for filter values
        StringBuilder innerWhereClause = new StringBuilder();

        if (predicatesMap.containsKey("variationFilterQuery")) {
            innerWhereClause.append("WHERE (")
                    .append(predicatesMap.get("variationFilterQuery"))
                    .append(") ");
        }
        if (predicatesMap.containsKey("priceAndStockFilterQuery")) {
            if (innerWhereClause.length() > 0) {
                innerWhereClause.append("AND (")
                        .append(predicatesMap.get("priceAndStockFilterQuery"))
                        .append(")");
            } else {
                innerWhereClause.append("WHERE (")
                        .append(predicatesMap.get("priceAndStockFilterQuery"))
                        .append(")");
            }
        }

        //  filtering ACTIVE & INACTIVE product items
        String productItemActiveFilter = queryDetails.isAllowInactiveProductItems()
                ? ""
                : "HAVING MAX(i.is_active) = 1";

        //  Base query
        String baseQuery = """
                FROM products p 
                INNER JOIN product_categories sc ON p.category_id = sc.id 
                INNER JOIN product_categories c ON sc.parent_category_id = c.id 
                LEFT JOIN (
                    SELECT 
                            i.product_id AS productId, 
                            COUNT(DISTINCT i.super_sku) AS colorCount, 
                            MIN(i.mrp) AS minMrp, 
                            MAX(i.mrp) AS maxMrp,
                            MIN(i.selling_price) AS minPrice, 
                            MAX(i.selling_price) AS maxPrice, 
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
                WHERE\s"""
                + searchResultProductIds
                + categoryFilters +
                """
                    sc.is_deleted = :isSubCategoryDeleted 
                    AND c.is_deleted = :isCategoryDeleted 
                    AND sc.is_active = :isSubCategoryActive 
                    AND c.is_active = :isCategoryActive 
                    AND minMrp IS NOT NULL 
                    AND maxMrp IS NOT NULL 
                    AND minPrice IS NOT NULL 
                    AND maxPrice IS NOT NULL 
                    AND minStockQty IS NOT NULL 
                    AND maxStockQty IS NOT NULL 
                    AND latestCreatedAt IS NOT NULL 
                    AND oldestCreatedAt IS NOT NULL 
                    AND activeStatus IS NOT NULL 
                """;

        //  ***** Count query *****
        String countQueryString = "SELECT COUNT(*) " + baseQuery;

        Query countQuery = entityManager.createNativeQuery(countQueryString);

        countQuery.setParameter("isSubCategoryDeleted", queryDetails.getStatusFlags().get("isSubCategoryDeleted"));
        countQuery.setParameter("isCategoryDeleted", queryDetails.getStatusFlags().get("isCategoryDeleted"));
        countQuery.setParameter("isSubCategoryActive", queryDetails.getStatusFlags().get("isSubCategoryActive"));
        countQuery.setParameter("isCategoryActive", queryDetails.getStatusFlags().get("isCategoryActive"));

        long totalElements = ((Number) countQuery.getSingleResult()).longValue();


        //  ***** main page query *****
        String mainPageQueryString = adminSelectClause + baseQuery;

        Query query = entityManager.createNativeQuery(mainPageQueryString, "AdminProductListingDtoMapping");

        query.setParameter("isSubCategoryDeleted", queryDetails.getStatusFlags().get("isSubCategoryDeleted"));
        query.setParameter("isCategoryDeleted", queryDetails.getStatusFlags().get("isCategoryDeleted"));
        query.setParameter("isSubCategoryActive", queryDetails.getStatusFlags().get("isSubCategoryActive"));
        query.setParameter("isCategoryActive", queryDetails.getStatusFlags().get("isCategoryActive"));

        int page = queryDetails.getPage();
        int size = queryDetails.getSize();

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        logger.info("fetching query content");
        List<AdminProductListingDto> pageContent = query.getResultList();

        //  ***** returning page object *****
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(pageContent, pageable, totalElements);
    }


    //  get product listing - for USER side
    @Override
    public Page<UserProductListingDto> findUserProductListing(QueryDetailsDto queryDetails) {

        //  SELECT clause
        String userSelectClause = """
                        SELECT 
                                p.id AS productId, 
                                p.product_name as productName, 
                                it.superSku, 
                                it.minMrp, 
                                it.maxMrp, 
                                it.minPrice, 
                                it.maxPrice, 
                                it.minStockQty, 
                                it.iconImage 
                """;

        //  generating WHERE clause for given FILTER VALUES
        Map<String, String> predicatesMap = new HashMap<>();
        if (queryDetails.getFilterValues() != null && !queryDetails.getFilterValues().isEmpty()) {
            logger.info("generating user filter predicates");
            predicatesMap = generatePredicates(queryDetails.getFilterValues());
        }

        //  WHERE clause for product IDs (search results)

        //  WHERE clause for category & sub-category filters

        //  WHERE clause for filter values
        StringBuilder variationAndPriceFilters = new StringBuilder();

        if (predicatesMap.containsKey("variationFilterQuery")) {
            variationAndPriceFilters.append("AND (")
                    .append(predicatesMap.get("variationFilterQuery"))
                    .append(")");
        }

        if (predicatesMap.containsKey("priceFilterQuery")) {
            variationAndPriceFilters.append("AND (")
                    .append(predicatesMap.get("priceFilterQuery"))
                    .append(")");
        }

        //  generating CATEGORY FILTER query
        String categoryFilterQuery = "";
        if (queryDetails.getCategoryName() != null && !queryDetails.getCategoryName().isEmpty()) {
            categoryFilterQuery = "AND LOWER(c.category_name) = '"
                    + queryDetails.getCategoryName().toLowerCase()
                    + "'";
        }
        StringBuilder subCategoryFilterQuery = new StringBuilder();
        if (queryDetails.getSubCategoryNames() != null && !queryDetails.getSubCategoryNames().isEmpty()) {
            subCategoryFilterQuery.append("AND LOWER(sc.category_name) IN (");

            List<String> lowerCased = queryDetails.getSubCategoryNames()
                    .stream()
                    .map(String::toLowerCase)
                    .toList();

            subCategoryFilterQuery.append("'")
                    .append(String.join("', '", lowerCased))
                    .append("')");
        }
        String categoryFilters = categoryFilterQuery + subCategoryFilterQuery;

        //  generating SORT query
        String sortingQuery = "";
        if (queryDetails.getSortRequest() != null && !queryDetails.getSortRequest().isEmpty()) {
            String sortField = queryDetails.getSortRequest().split(",")[0];
            String sortDirection = queryDetails.getSortRequest().split(",")[1];

            sortingQuery = "ORDER BY " + sortField + " " + sortDirection;
        }

        //  Building Base query
        String baseQuery = """                       
                FROM products p 
                INNER JOIN product_categories sc ON p.category_id = sc.id 
                INNER JOIN product_categories c ON sc.parent_category_id = c.id 
                LEFT JOIN ( 
                    SELECT 
                            i.product_id AS productId, 
                            i.super_sku AS superSku,
                            MIN(i.mrp) AS minMrp, 
                            MAX(i.mrp) AS maxMrp, 
                            MIN(i.selling_price) AS minPrice, 
                            MAX(i.selling_price) AS maxPrice, 
                            MIN(i.created_at) AS oldestCreatedAt, 
                            MAX(i.created_at) AS latestCreatedAt, 
                            MIN(i.qty_in_stock) AS minStockQty, 
                            im.image_url AS iconImage 
                        FROM product_items i 
                        JOIN product_configurations pc ON i.id = pc.product_item_id 
                        JOIN variation_options o ON pc.variation_option_id = o.id 
                        JOIN variations v ON o.variation_id = v.id 
                        JOIN item_image_configuration iic ON i.id = iic.item_id 
                        JOIN product_images im ON iic.image_id = im.id 
                        WHERE 
                            i.is_active = :isItemActive 
                """ + variationAndPriceFilters + """
                        GROUP BY i.product_id 
                ) AS it ON p.id = it.productId 
                WHERE p.pod_available = :podAvailable 
                """ + categoryFilters + """
                    AND c.is_deleted = :isCategoryDeleted 
                    AND sc.is_deleted = :isSubCategoryDeleted 
                    AND c.is_active = :isCategoryActive 
                    AND sc.is_active = :isSubCategoryActive 
                    AND minPrice IS NOT NULL 
                    AND maxPrice IS NOT NULL 
                    AND minStockQty IS NOT NULL 
                    AND latestCreatedAt IS NOT NULL 
                    AND oldestCreatedAt IS NOT NULL 
                """ + sortingQuery;

        //  ***** Count query *****
        String countQueryString = "SELECT COUNT(*) " + baseQuery;

        Query countQuery = entityManager.createNativeQuery(countQueryString);

        countQuery.setParameter("isItemActive", queryDetails.getStatusFlags().get("isItemActive"));
        countQuery.setParameter("podAvailable", queryDetails.getStatusFlags().get("podAvailable"));

        countQuery.setParameter("isCategoryDeleted", queryDetails.getStatusFlags().get("isCategoryDeleted"));
        countQuery.setParameter("isSubCategoryDeleted", queryDetails.getStatusFlags().get("isSubCategoryDeleted"));
        countQuery.setParameter("isCategoryActive", queryDetails.getStatusFlags().get("isCategoryActive"));
        countQuery.setParameter("isSubCategoryActive", queryDetails.getStatusFlags().get("isSubCategoryActive"));

        long totalElements = ((Number) countQuery.getSingleResult()).longValue();

        // ***** main page query *****

        String mainQueryString = userSelectClause + baseQuery;

        Query query = entityManager.createNativeQuery(mainQueryString, "UserProductListingDtoMapping");

        query.setParameter("isItemActive", queryDetails.getStatusFlags().get("isItemActive"));
        query.setParameter("podAvailable", queryDetails.getStatusFlags().get("podAvailable"));

        query.setParameter("isCategoryDeleted", queryDetails.getStatusFlags().get("isCategoryDeleted"));
        query.setParameter("isSubCategoryDeleted", queryDetails.getStatusFlags().get("isSubCategoryDeleted"));
        query.setParameter("isCategoryActive", queryDetails.getStatusFlags().get("isCategoryActive"));
        query.setParameter("isSubCategoryActive", queryDetails.getStatusFlags().get("isSubCategoryActive"));

        int page = queryDetails.getPage();
        int size = queryDetails.getSize();

        query.setFirstResult(page * size);
        query.setMaxResults(size);

        logger.info("fetching query content - user product listing");
        List<UserProductListingDto> pageContent = query.getResultList();

        //  ***** returning page object *****
        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(pageContent, pageable, totalElements);
    }


    /// /  ********* Utility methods *********

    private String productIdsPredicate(List<UUID> productIds) {
        String idString = productIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        return "p.id IN (" + String.join(", ", idString) + ") AND";
    }

    private Map<String, String> generatePredicates(List<FilterRequestDto> filterValues) {

        Map<String, String> predicatesMap = new HashMap<>();

        //  getting all variation names
        List<String> variationNames = variationQueryService.getAllVariationsWithoutOptions()
                .stream()
                .map(v -> v.getVariationName().toLowerCase())
                .toList();

        //  generating predicates for 'variations' filter values
        List<FilterRequestDto> variationFilters = filterValues.stream()
                .filter(f -> variationNames.contains(f.getFilterType().toLowerCase()))
                .toList();
        String variationFilterQuery = generateVariationPredicates(variationFilters);
        if (!variationFilterQuery.isEmpty()) predicatesMap.put("variationFilterQuery", variationFilterQuery);

        //  generating predicates for 'price-range' & 'stock' filter values
        List<FilterRequestDto> priceAndStockFilters = filterValues.stream()
                .filter(f -> f.getFilterType().equalsIgnoreCase("price")
                        || f.getFilterType().equalsIgnoreCase("stock"))
                .toList();

        //  checking if only 'price' filter or 'price' & 'stock' filter
        AtomicBoolean priceOnly = new AtomicBoolean(true);
        priceAndStockFilters.stream()
                .filter(f -> f.getFilterType().equalsIgnoreCase("stock"))
                .forEach(f -> priceOnly.set(false));
        if (priceOnly.get()) {
            String priceFilterQuery = generatePriceAndStockPredicates(priceAndStockFilters, priceOnly.get());
            if (!priceFilterQuery.isEmpty()) {
                predicatesMap.put("priceFilterQuery", priceFilterQuery);
            }
        } else {
            String priceAndStockFilterQuery = generatePriceAndStockPredicates(priceAndStockFilters, priceOnly.get());
            if (!priceAndStockFilterQuery.isEmpty()) {
                predicatesMap.put("priceAndStockFilterQuery", priceAndStockFilterQuery);
            }
        }

        return predicatesMap;
    }


    //  generate query to filter variation values
    private String generateVariationPredicates(List<FilterRequestDto> variationFilters) {
        logger.info("generating variations filter predicates for " + variationFilters.size() + " variations");
        return variationFilters.stream()
                .map(f -> {

                    //  processing filter values
                    List<String> correctedValues = Arrays.stream(f.getValues().split(", "))     //  ## take care f the ", " & ",\\s*"
                            .map(String::trim)
                            .map(value -> "'" + value.toLowerCase() + "'")
                            .toList();

                    String filterValue = String.join(", ", correctedValues);

                    return "(LOWER(v.variation_name) = '" + f.getFilterType().toLowerCase() +
                            "' AND LOWER(o.option_value) IN (" + filterValue + "))";
                }).collect(Collectors.joining(" OR "));
    }


    //  generate query to filter PRICE & STOCK values
    private String generatePriceAndStockPredicates(List<FilterRequestDto> priceAndStockFilters,
                                                   boolean priceOnly) {

        List<String> predicates = new ArrayList<>();

        if (!priceOnly) {
            logger.info("generating price and stock predicates");

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
        } else {
            logger.info("generating price predicates");
        }

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

        return String.join(" OR ", predicates);
    }

}


