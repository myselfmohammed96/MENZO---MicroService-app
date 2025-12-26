package com.menzo.Product_Service.Modules.Discount.Service;

import com.menzo.Product_Service.Modules.Discount.Dto.DiscountListingDto;
import com.menzo.Product_Service.Modules.Discount.Entity.Discount;
import com.menzo.Product_Service.Modules.Discount.Enum.PromotionStatus;
import com.menzo.Product_Service.Modules.Discount.Repo.DiscountRepo;
import com.menzo.Product_Service.Modules.SearchAndFilter.Dto.RequestDto;
import org.aspectj.apache.bcel.generic.RET;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountQueryService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountQueryService.class);

    @Autowired
    private DiscountRepo discountRepo;

    @Autowired
    private DiscountSpecService specService;


    //  ## remove isDeleted fields
    //  Discount listing (with SORTING, FILTER)
    public Page<?> getDiscountListing(Integer page,
                                      Integer size,
                                      String sortRequest,
                                      RequestDto filterRequest) {

        if (sortRequest == null) {
            throw new IllegalArgumentException("Sort request cannot be null");
        }
        Sort sort = generateDiscountSort(sortRequest);

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Page<Discount> pageContent;
        if (filterRequest == null) {
            pageContent = discountRepo.findAll(pageable);
        } else {
            Specification<Discount> discountSpecs = specService.getFilterSpec(filterRequest.getFilterRequestDtos());
            pageContent = discountRepo.findAll(discountSpecs, pageable);
        }
        List<DiscountListingDto> listingDtos = pageContent.stream()
                .map(this::convertToListingDto)
                .collect(Collectors.toList());

        return new PageImpl<>(
                listingDtos,
                pageable,
                pageContent.getTotalElements()
        );
    }


    /// /   ********* Utility methods *********

    //  generates discount SORT
    private Sort generateDiscountSort(String sortRequest) {
        //  ## sort by value & capValue (optional)
        //  Simple sort
        Sort simpleSort = resolveSimpleSort(sortRequest);
        if (simpleSort != null) {
            return simpleSort;
        }

        //  Enum sort
        Sort enumSort = resolveEnumSort(sortRequest);
        if (enumSort != null) {
            return enumSort;
        }

        //  Default sort
        return Sort.by("createdAt").descending();

    }

    //  provides Sort for SIMPLE fields
    private Sort resolveSimpleSort(String sortRequest) {
        switch (sortRequest) {
            case "latest":
                return Sort.by(Sort.Direction.DESC, "createdAt");
            case "oldest":
                return Sort.by(Sort.Direction.ASC, "createdAt");
            case "name,asc":
                return Sort.by("discountName").ascending();
            case "name,desc":
                return Sort.by("discountName").descending();
            case "code,asc":
                return Sort.by("discountCode").ascending();
            case "code,desc":
                return Sort.by("discountCode").descending();
            case "start,asc":
                return Sort.by("startAt").ascending();
            case "start,desc":
                return Sort.by("startAt").descending();
            case "end,asc":
                return Sort.by("endAt").ascending();
            case "end,desc":
                return Sort.by("endAt").descending();
            case "resume,asc":
                return Sort.by("resumeAt").ascending();
            case "resume,desc":
                return Sort.by("resumeAt").descending();
            case "priority,asc":
                return Sort.by("priority").ascending();
            case "priority,desc":
                return Sort.by("priority").descending();
            default:
                return null;
        }
    }

    //  provides Sort for ENUM fields
    private Sort resolveEnumSort(String sortRequest) {

        //  discountStatus sorting
        if (sortRequest.equalsIgnoreCase("status,asc")) {
            return JpaSort.unsafe("""
                    CASE discount_status 
                        WHEN 'ACTIVE' THEN 1 
                        WHEN 'SCHEDULED' THEN 2 
                        WHEN 'PAUSED' THEN 3 
                        WHEN 'INACTIVE' THEN 4 
                        WHEN 'EXPIRED' THEN 5 
                        WHEN 'CANCELLED' THEN 6 
                    END
                    """);
        }
        if (sortRequest.equalsIgnoreCase("status,desc")) {
            return JpaSort.unsafe("""
                    CASE discount_status 
                        WHEN 'CANCELLED' THEN 1 
                        WHEN 'EXPIRED' THEN 2 
                        WHEN 'INACTIVE' THEN 3 
                        WHEN 'PAUSED' THEN 4 
                        WHEN 'SCHEDULED' THEN 5 
                        WHEN 'ACTIVE' THEN 6 
                    END
                    """);
        }

        //  level (DiscountLevel) sorting
        if (sortRequest.equalsIgnoreCase("level,asc")) {
            return JpaSort.unsafe("""
                    CASE discount_level 
                        WHEN 'GLOBAL' THEN 1 
                        WHEN 'CATEGORY' THEN 2 
                        WHEN 'SUB_CATEGORY' THEN 3 
                        WHEN 'PRODUCT' THEN 4 
                        WHEN 'VARIANT' THEN 5
                    END
                    """);
        }
        if (sortRequest.equalsIgnoreCase("level,desc")) {
            return JpaSort.unsafe("""
                    CASE discount_level 
                        WHEN 'VARIANT' THEN 1
                        WHEN 'PRODUCT' THEN 2
                        WHEN 'SUB_CATEGORY' THEN 3 
                        WHEN 'CATEGORY' THEN 4 
                        WHEN 'GLOBAL' THEN 5
                    END
                    """);
        }

        //  type (DiscountType) sorting
        if (sortRequest.equalsIgnoreCase("type,asc")) {
            return JpaSort.unsafe("""
                    CASE discount_type 
                        WHEN 'PERCENT' THEN 1
                        WHEN 'FLAT' THEN 2
                    END
                    """);
        }
        if (sortRequest.equalsIgnoreCase("type,desc")) {
            return JpaSort.unsafe("""
                    CASE discount_type
                        WHEN 'FLAT' THEN 1 
                        WHEN 'PERCENT' THEN 2
                    END
                    """);
        }
        return null;
    }

    //  Convert: Discount -> DiscountListingDto
    private DiscountListingDto convertToListingDto(Discount discount) {
        try {
            PromotionStatus status = validateStatus(discount);

            return DiscountListingDto.builder()
                    .discountId(discount.getId())
                    .discountCode(discount.getDiscountCode())
                    .discountName(discount.getDiscountName())
                    .level(discount.getLevel())
                    .type(discount.getType())
                    .value(discount.getValue())
                    .status(status)
                    .build();
        } catch (Exception e) {
            logger.error("Error converting Discount to DiscountListingDto, Disocunt ID: {}", discount.getId(), e);
            return null;
        }
    }

    //  validate discount status: Safety check
    private PromotionStatus validateStatus(Discount discount) {
        LocalDateTime now = LocalDateTime.now();
        if (discount.getDiscountStatus() != PromotionStatus.PAUSED
                && discount.getDiscountStatus() != PromotionStatus.INACTIVE
                && discount.getDiscountStatus() != PromotionStatus.CANCELLED) {

            if (now.isBefore(discount.getStartAt())
                    && discount.getDiscountStatus() != PromotionStatus.SCHEDULED) {
                logger.warn("Discount status mismatch: Changing status, {} -> SCHEDULED", discount.getDiscountStatus());
                discount.setDiscountStatus(PromotionStatus.SCHEDULED);
                Discount updated = discountRepo.save(discount);
                return updated.getDiscountStatus();

            } else if (discount.getStartAt().isBefore(now)
                    && discount.getEndAt().isAfter(now)
                    && discount.getDiscountStatus() != PromotionStatus.ACTIVE) {
                logger.warn("Discount status mismatch: Changing status, {} -> ACTIVE", discount.getDiscountStatus());
                discount.setDiscountStatus(PromotionStatus.ACTIVE);
                Discount updated = discountRepo.save(discount);
                return updated.getDiscountStatus();

            } else if (discount.getEndAt().isBefore(now)
                    && discount.getDiscountStatus() != PromotionStatus.EXPIRED) {
                logger.warn("Discount status mismatch: Changing status, {} -> EXPIRED", discount.getDiscountStatus());
                discount.setDiscountStatus(PromotionStatus.EXPIRED);
                Discount updated = discountRepo.save(discount);
                return updated.getDiscountStatus();

            } else {
                return discount.getDiscountStatus();
            }
        } else {
            return discount.getDiscountStatus();
        }
    }

}
