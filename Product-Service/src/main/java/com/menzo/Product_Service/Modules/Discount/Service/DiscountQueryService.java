package com.menzo.Product_Service.Modules.Discount.Service;

import com.menzo.Product_Service.Modules.Category.Dto.ParentCategoryDto;
import com.menzo.Product_Service.Modules.Category.Dto.SubCategoryDto;
import com.menzo.Product_Service.Modules.Category.Service.CategoryQueryService;
import com.menzo.Product_Service.Modules.Discount.Dto.*;
import com.menzo.Product_Service.Modules.Discount.Entity.Discount;
import com.menzo.Product_Service.Modules.Discount.Entity.DiscountCategory;
import com.menzo.Product_Service.Modules.Discount.Entity.DiscountVariant;
import com.menzo.Product_Service.Modules.Discount.Enum.*;
import com.menzo.Product_Service.Modules.Discount.Repo.DiscountRepo;
import com.menzo.Product_Service.Modules.Product.Dto.ItemDto.ItemMinDto;
import com.menzo.Product_Service.Modules.Product.Dto.ProductMinDto;
import com.menzo.Product_Service.Modules.Product.Service.ProductsQueryService;
import com.menzo.Product_Service.Modules.SearchAndFilter.Dto.RequestDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiscountQueryService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountQueryService.class);

    @Autowired
    private DiscountRepo discountRepo;

    @Autowired
    private DiscountSpecService specService;

    @Autowired
    private CategoryQueryService categoryQueryService;

    @Autowired
    private ProductsQueryService productsQueryService;


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

    //  Discount summary
    public DiscountSummaryDto getDiscountSummary(UUID discountId) {
        Discount discount = discountRepo.findById(discountId)
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with ID: " + discountId));

        String description = discount.getDiscountDescription() != null && !discount.getDiscountDescription().trim().isEmpty()
                ? discount.getDiscountDescription()
                : null;
        return DiscountSummaryDto.builder()
                .discountId(discount.getId())
                .discountCode(discount.getDiscountCode())
                .discountName(discount.getDiscountName())
                .discountDescription(description)
                .level(discount.getLevel())
                .type(discount.getType())
                .value(discount.getValue())
                .capType(discount.getCapType())
                .capValue(discount.getCapValue())
                .priority(discount.getPriority())
                .startAt(discount.getStartAt())
                .endAt(discount.getEndAt())
                .status(discount.getDiscountStatus())
                .resumeAt(discount.getResumeAt())
                .createdAt(discount.getCreatedAt())
                .updatedAt(discount.getUpdatedAt())
                .build();
    }

    //  Discount mapped content
    //  ## have to provide exclusions, sorting and filtering for discount mapped content
    public List<MappedContentDto> getDiscountMappedContent(UUID discountId,
                                                           String sortRequest,
                                                           RequestDto filterRequest) {
        Discount discount = discountRepo.findById(discountId)
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with ID: " + discountId));
        return switch (discount.getLevel()) {
            case GLOBAL -> List.of();
            case CATEGORY -> discount.getDiscountCategories().stream()
                    .filter(this::isParentCategory)
                    .map(dc -> toMappedContent(dc.getId(), dc.getCategory().getCategoryName()))
                    .toList();
            case SUB_CATEGORY -> discount.getDiscountCategories().stream()
                    .filter(this::isSubCategory)
                    .map(dc -> toMappedContent(dc.getId(), dc.getCategory().getCategoryName()))
                    .toList();
            case PRODUCT -> discount.getDiscountProducts().stream()
                    .map(dp -> toMappedContent(dp.getId(), dp.getProduct().getProductName()))
                    .toList();
            case VARIANT -> discount.getDiscountVariants().stream()
                    .map(this::mapVariant)
                    .toList();
            default -> throw new RuntimeException("Invalid discount level");
        };
    }


    /// /   ********* Dto data *********

    //  get Discount level
    public EnumDto getDiscountLevel() {
        List<String> values = Arrays.stream(DiscountLevel.values())
                .map(Enum::name)
                .toList();

        return EnumDto.builder()
                .enumName(DiscountLevel.class.getSimpleName())
                .enumValues(values)
                .build();
    }

    //  get Discount type
    public EnumDto getDiscountType() {
        List<String> values = Arrays.stream(DiscountType.values())
                .map(Enum::name)
                .toList();

        return EnumDto.builder()
                .enumName(DiscountType.class.getSimpleName())
                .enumValues(values)
                .build();
    }

    //  get Cap type
    public EnumDto getCapType() {
        List<String> values = Arrays.stream(CapType.values())
                .map(Enum::name)
                .toList();

        return EnumDto.builder()
                .enumName(CapType.class.getSimpleName())
                .enumValues(values)
                .build();
    }

    //  get Discount status
    public EnumDto getDiscountStatus(DiscountStatusTarget target) {
        List<PromotionStatus> whiteList = switch (target) {
            case FORM -> List.of(
                    PromotionStatus.ACTIVE,
                    PromotionStatus.INACTIVE
            );
            case SUMMARY -> List.of(
                    PromotionStatus.ACTIVE,
                    PromotionStatus.INACTIVE,
                    PromotionStatus.PAUSED,
                    PromotionStatus.CANCELLED
            );
            default -> throw new IllegalArgumentException(
                    "Invalid target for DiscountStatus: " + target
            );
        };

        List<String> values = whiteList.stream()
                .map(Enum::name)
                .toList();

        return EnumDto.builder()
                .enumName("DiscountStatus")
                .enumValues(values)
                .build();
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


    //  ------- getDiscountMappedContent utility methods -------

    //  check if mapped category is a parent category
    private boolean isParentCategory(DiscountCategory dc) {
        if (dc.getCategory().getParentCategoryId() != null) {
            logger.error("Sub-category found in Category mapping with mapping ID: {}", dc.getId());
            return false;
        }
        return true;
    }

    //  check if mapped category is a sub-category
    private boolean isSubCategory(DiscountCategory dc) {
        if (dc.getCategory().getParentCategoryId() == null) {
            logger.error("Parent category found in Sub-category mapping with mapping ID: {}", dc.getId());
            return false;
        }
        return true;
    }

    //  mappedContentDto for CATEGORY, SUB_CATEGORY & PRODUCT
    private MappedContentDto mapVariant(DiscountVariant dv) {
        Map<String, String> mm = dv.getProductItem().getConfigurations().stream()
                .map(config -> Map.entry(
                        config.getVariationOption()
                                .getVariation()
                                .getVariationName(),
                        config.getVariationOption()
                                .getOptionValue()
                ))
                .filter(e -> e.getKey().equals("colors") || e.getKey().equals("size"))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a
                ));
        return MappedContentDto.builder()
                .mappingId(dv.getId())
                .textContent(dv.getProductItem().getSKU())
                .color(mm.get("colors"))
                .size(mm.get("size"))
                .build();
    }

    //  mappedContentDto for VARIANT
    private MappedContentDto toMappedContent(UUID id, String text) {
        return MappedContentDto.builder()
                .mappingId(id)
                .textContent(text)
                .build();
    }

    public List<LevelDetailsDto> getLevelDetails(@Nullable Long id,
                                                 DiscountLevel currentLevel) {
//        if (targetLevel == currentLevel) {
//        }
        switch (currentLevel) {
            case CATEGORY -> {
                List<ParentCategoryDto> categories = categoryQueryService.getAllParents();
                return categories.stream()
                        .map(c -> LevelDetailsDto.builder()
                                .id(c.getId())
                                .text(c.getCategoryName())
                                .build()
                        ).toList();
            }
            case SUB_CATEGORY -> {
                List<SubCategoryDto> subCategories = categoryQueryService.getAllSubOfParentId(id);
                return subCategories.stream()
                        .map(s -> LevelDetailsDto.builder()
                                .id(s.getId())
                                .text(s.getCategoryName())
                                .build()
                        ).toList();
            }
            case PRODUCT -> {
                List<ProductMinDto> products = productsQueryService.getProductsBySubCategory(id);
                return products.stream()
                        .map(p -> LevelDetailsDto.builder()
                                .id(p.getProductId())
                                .text(p.getProductName())
                                .imageIcon(p.getIconImage())
                                .build()
                        ).toList();
            }
            case VARIANT -> {
                List<ItemMinDto> items = productsQueryService.getProductItemByProductId(id);
                return items.stream()
                        .map(i -> LevelDetailsDto.builder()
                                .id(i.getItemId())
                                .text(i.getSku())
                                .imageIcon(i.getImageUrl())
                                .size(i.getSize())
                                .color(i.getColorName())
                                .hexCode(i.getHexCode())
                                .build()
                        ).toList();
            }
            default -> throw new IllegalArgumentException("Invalid discount level: " + currentLevel);
        }
    }

    public boolean checkDiscountCodeExist(DiscountCodeDto code) {
        return discountRepo.existsByDiscountCode(code.getDiscountCode());
    }

}
