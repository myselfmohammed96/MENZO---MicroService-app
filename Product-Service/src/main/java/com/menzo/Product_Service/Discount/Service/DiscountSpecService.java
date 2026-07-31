package com.menzo.Product_Service.Discount.Service;

import com.menzo.Product_Service.Discount.Entity.Discount;
import com.menzo.Product_Service.Discount.Enum.*;
import com.menzo.Product_Service.Modules.Discount.Enum.*;
import com.menzo.Product_Service.Modules.SearchAndFilter.Dto.FilterRequestDto;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class DiscountSpecService {

    //  generate Discount specification
    public Specification<Discount> getFilterSpec(List<FilterRequestDto> filterRequestDtos) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            //  generate predicates
            Map<String, Function<FilterRequestDto, Predicate>> filterMap = Map.of(
                    "level", r -> root.get("level").in(toEnum(DiscountLevel.class, r.getValues())),
                    "type", r -> root.get("type").in(toEnum(DiscountType.class, r.getValues())),
                    "status", r -> root.get("discountStatus").in(toEnum(PromotionStatus.class, r.getValues())),
                    "capType", r -> root.get("capType").in(toEnum(CapType.class, r.getValues())),
                    "priority", r -> root.get("priority").in(convertToIntLIst(r.getValues()))
//                    "startAt", r -> null,
//                    "endAt", r -> null,
//                    "resumeAt", r -> null
            );

            List<Predicate> predicates = new ArrayList<>();

            for (FilterRequestDto request : filterRequestDtos) {
                Function<FilterRequestDto, Predicate> function = filterMap.get(request.getFilterType());

                if (function == null) {
                    throw new IllegalArgumentException("Unsupported filter type: " + request.getFilterType());
                }
                predicates.add(function.apply(request));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


    /// /   ********* Utility methods *********

    //  Sprint splitter
    private List<String> split(String valuesString) {
        return Arrays.stream(valuesString.split(","))
                .map(String::trim)
                .toList();
    }

    //  Convert String to Integer list
    private List<Integer> convertToIntLIst(String valuesString) {
        return split(valuesString).stream()
                .map(v -> {
                    if (!v.matches("\\d+")) {       //  -?\\d+ for -ve int
                        throw new IllegalArgumentException("Invalid value. Must be integer. " + v);
                    }
                    return Integer.parseInt(v);
                }).toList();
    }

    //  Convert String to Enum list
    private <E extends Enum<E>> List<E> toEnum(Class<E> enumClass, String value) {
        return split(value).stream()
                .map(s -> EnumUtil.toEnum(enumClass, s))
                .toList();
    }

}
