package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.SpecificationsDto.FilterRequestDto;
import com.menzo.Product_Service.Entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductSpecificationService<T> {

    public Specification<T> getFilterSpecification(List<FilterRequestDto> filterRequestDtos) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            Map<String, Join<?, ?>> productJoins = productJoinBuilder(root);
            List<Predicate> predicates = new ArrayList<>();

            for(FilterRequestDto requestDto : filterRequestDtos) {

                if ("price".equals(requestDto.getFilterType().toLowerCase())) {
                    Predicate pricePredicate = priceRangePredicate(productJoins.get("items"), criteriaBuilder, requestDto.getFilterType(), requestDto.getValues());
                    predicates.add(pricePredicate);
                } else if ("colors".equals(requestDto.getFilterType().toLowerCase()) ||
                    "size".equals(requestDto.getFilterType().toLowerCase()) ||
                    "fit type".equals(requestDto.getFilterType().toLowerCase())) {

                    Predicate variationNamePredicate = criteriaBuilder.equal(productJoins.get("variations").get("variationName"), requestDto.getFilterType());
                    predicates.add(variationNamePredicate);

                    Predicate variationOptionsPredicate = productJoins.get("options").get("optionValue")
                            .in(stringSplitter(requestDto.getValues()));
                    predicates.add(variationOptionsPredicate);
                } else {
                    throw new IllegalArgumentException("Unsupported filter type: " + requestDto.getFilterType());
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    //   Creates collective predicate for different price ranges
    private Predicate priceRangePredicate(Join<?, ?> itemsJoin, CriteriaBuilder criteriaBuilder, String filterType, String priceRanges) {
        List<String> rangesList = stringSplitter(priceRanges);
        Predicate[] priceRangePredicates = new Predicate[rangesList.size()];
        int arrayIndex = 0;

        for(String range : rangesList) {
            String[] ranges = range.split("to");
            Predicate priceRangePredicate = criteriaBuilder.between(itemsJoin.get(filterType.toLowerCase()), Long.parseLong(ranges[0].trim()), Long.parseLong(ranges[1].trim()));
            priceRangePredicates[arrayIndex] = priceRangePredicate;
            arrayIndex++;
        }

        return criteriaBuilder.or(priceRangePredicates);
    }

    //  Splits stringed list with regex ", "
    private List<String> stringSplitter(String string) {
        String[] strings = string.split(",");
        List<String> stringsList = new ArrayList<>();

        for(String str : strings) {
            stringsList.add(str.trim());
        }
        return stringsList;
    }

    //  Join builder for Product entities
    private Map<String, Join<?, ?>> productJoinBuilder(Root<?> root) {
        Class<?> entityType = root.getJavaType();
        return switch (entityType.getSimpleName()) {
            case "Product" -> {
                Map<String, Join<?, ?>> joinMap = new HashMap<>();

                Join<Product, ProductItem> itemsJoin = root.join("items", JoinType.INNER);
                joinMap.put("items", itemsJoin);

                Join<ProductItem, ProductConfiguration> configsJoin = itemsJoin.join("configurations", JoinType.INNER);
                joinMap.put("configs", configsJoin);

                Join<ProductConfiguration, VariationOption> optionsJoin = configsJoin.join("variationOption", JoinType.INNER);
                joinMap.put("options", optionsJoin);

                Join<VariationOption, Variation> variationsJoin = optionsJoin.join("variation", JoinType.INNER);
                joinMap.put("variations", variationsJoin);

                yield joinMap;
            }

            default -> throw new IllegalArgumentException("Unsupported entity type: " + entityType);
        };
    }

}
