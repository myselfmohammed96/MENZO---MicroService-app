package com.menzo.Product_Service.Discount.Service;

import com.menzo.Product_Service.Category.Entity.ProductCategory;
import com.menzo.Product_Service.Category.Repository.CategoriesRepository;
import com.menzo.Product_Service.Discount.Dto.CreateDiscountDto;
import com.menzo.Product_Service.Discount.Dto.DiscountMappingDto;
import com.menzo.Product_Service.Discount.Dto.MappedContentDto;
import com.menzo.Product_Service.Discount.Dto.UpdateDiscountDto;
import com.menzo.Product_Service.Discount.Entity.Discount;
import com.menzo.Product_Service.Discount.Entity.DiscountCategory;
import com.menzo.Product_Service.Discount.Entity.DiscountProduct;
import com.menzo.Product_Service.Discount.Entity.DiscountVariant;
import com.menzo.Product_Service.Discount.Enum.CapType;
import com.menzo.Product_Service.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Discount.Enum.DiscountType;
import com.menzo.Product_Service.Discount.Enum.OperationalStatus;
import com.menzo.Product_Service.Discount.Repository.DiscountRepository;
import com.menzo.Product_Service.Product.Entity.Product;
import com.menzo.Product_Service.Product.Entity.ProductItem;
import com.menzo.Product_Service.Product.Repository.ProductItemsRepository;
import com.menzo.Product_Service.Product.Repository.ProductsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DiscountCommandService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountCommandService.class);

    @Autowired
    private DiscountRepository discountRepo;

    @Autowired
    private CategoriesRepository categoriesRepo;

    @Autowired
    private ProductsRepository productsRepo;

    @Autowired
    private ProductItemsRepository itemsRepo;


    /*
     *
     *   Add new discount
     *
     */
    public UUID addNewDiscount(CreateDiscountDto dto) {

        //  data validation
        if (discountRepo.existsByDiscountCode(dto.getDiscountCode())) {
            logger.warn("Duplicate discount code: Discount already exists with code {}", dto.getDiscountCode());
            throw new IllegalArgumentException("Invalid code: Discount already exists with code: " + dto.getDiscountCode());
        }
        if (dto.getDiscountType() == DiscountType.PERCENT && (dto.getDiscountValue().compareTo(BigDecimal.valueOf(100)) > 0)) {
            throw new IllegalArgumentException("Invalid input: Discount value should be less than 100%");
        }
        if (dto.getCapType() == CapType.PERCENT && (dto.getCapValue().compareTo(BigDecimal.valueOf(100)) > 0)) {
            throw new IllegalArgumentException("Invalid input: Discount cap value should be less than 100%");
        }
        //  ## capType x capValue validation (for type NONE)
        if (!dto.getStartAt().isBefore(dto.getEndAt())) {
            throw new IllegalArgumentException("Invalid input: Discount start date should be before End date.");
        }
        if (dto.getEndAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid input: End date cannot be in the past");
        }

        //  status evaluation
        LocalDateTime now = LocalDateTime.now();
        OperationalStatus status;

        if (dto.getDiscountStatus() == OperationalStatus.INACTIVE) {
            status = OperationalStatus.INACTIVE;
        } else if (dto.getStartAt().isAfter(now)) {
            status = OperationalStatus.SCHEDULED;
        } else if (dto.getStartAt().isBefore(now) && dto.getEndAt().isAfter(now)) {
            status = OperationalStatus.ACTIVE;
        } else {
            status = OperationalStatus.INACTIVE;
        }

        //  saving discount
        Discount discount = Discount.builder()
                .discountCode(dto.getDiscountCode())
                .discountName(dto.getDiscountName())
                .discountDescription(dto.getDiscountDescription())
                .discountLevel(dto.getDiscountLevel())
                .discountType(dto.getDiscountType())
                .discountValue(dto.getDiscountValue())
                .capType(dto.getCapType())
                .capValue(dto.getCapValue())
                .priority(dto.getPriority())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .discountStatus(status)
                .build();

        return discountRepo.save(discount).getDiscountId();
    }


    /*
     *
     *   Update discount (partial/patch update)
     *   ## user must be able to change the discountLevel and discountCode and discountType
     *
     */
    public boolean updateDiscount(UUID discountId, UpdateDiscountDto latestDiscountDto) {
        //  fetching discount by discount ID
        Discount discount = discountRepo.findById(discountId)
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with ID: " + discountId));

        //  updating discount name
        if (latestDiscountDto.getDiscountName() != null && !latestDiscountDto.getDiscountName().isEmpty()) {
            if (latestDiscountDto.getDiscountName().length() < 5 || latestDiscountDto.getDiscountName().length() > 100) {
                throw new IllegalArgumentException("Discount name must be 5 to 100 characters long");
            }
            discount.setDiscountName(latestDiscountDto.getDiscountName());
        }

        //  updating discount description
        if (latestDiscountDto.getDiscountDescription() != null && !latestDiscountDto.getDiscountDescription().isEmpty()) {
            if (latestDiscountDto.getDiscountDescription().length() > 255) {
                throw new IllegalArgumentException("Discount description must not exceed 255 characters");
            }
            discount.setDiscountDescription(latestDiscountDto.getDiscountDescription());
        }

        //  updating discount value
        if (latestDiscountDto.getDiscountValue() != null) {
            if (latestDiscountDto.getDiscountValue().compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new IllegalArgumentException("Discount value should be a positive number");
            }
            if (discount.getDiscountType() == DiscountType.PERCENT
                    && (latestDiscountDto.getDiscountValue().compareTo(BigDecimal.valueOf(100)) > 0)) {
                throw new IllegalArgumentException("Discount value should be less than 100%");
            }
            discount.setDiscountValue(latestDiscountDto.getDiscountValue());
        }

        //  updating cap type
        if (latestDiscountDto.getCapType() != null) {
            discount.setCapType(latestDiscountDto.getCapType());
        }

        //  updating cap value
        if (latestDiscountDto.getCapValue() != null) {
            if (discount.getCapType() == CapType.NONE) {
                throw new IllegalArgumentException("Discount cap value is provided. Discount cap type must not be NONE.");
            }
            if (latestDiscountDto.getCapValue().compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new IllegalArgumentException("Discount cap value should be a positive number");
            }
            if (discount.getCapType() == CapType.PERCENT && (latestDiscountDto.getCapValue().compareTo(BigDecimal.valueOf(100)) > 0)) {
                throw new IllegalArgumentException("Discount cap value should be less than 100%");
            }
            discount.setCapValue(latestDiscountDto.getCapValue());
        }

        //  updating priority
        if (latestDiscountDto.getPriority() != null) {
            if (latestDiscountDto.getPriority() == 0) {
                throw new IllegalArgumentException("Discount priority should not be less than 0");
            }
            discount.setPriority(latestDiscountDto.getPriority());
        }

        LocalDateTime now = LocalDateTime.now();

        //  updating start at
        if (latestDiscountDto.getStartAt() != null) {
            if (discount.getStartAt().isBefore(now)) {
                throw new IllegalArgumentException("Discount already activated. Cannot update Start date.");
            }
            if (latestDiscountDto.getStartAt().isAfter(discount.getEndAt())) {
                throw new IllegalArgumentException("Discount start date cannot be after the end date");
            }
            if (latestDiscountDto.getEndAt() != null && latestDiscountDto.getStartAt().isAfter(latestDiscountDto.getEndAt())) {
                throw new IllegalArgumentException("Discount start date cannot be after then new end date");
            }
            if (latestDiscountDto.getStartAt().isBefore(now)) {
                throw new IllegalArgumentException("Discount start date cannot be in past");
            }
            discount.setStartAt(latestDiscountDto.getStartAt());
        }

        //  updating end at
        if (latestDiscountDto.getEndAt() != null) {
            if (discount.getEndAt().isBefore(now)) {
                throw new IllegalArgumentException("Discount already expired. Cannot update End date.");
            }
            if (latestDiscountDto.getEndAt().isBefore(discount.getStartAt())) {
                throw new IllegalArgumentException("Discount end date cannot be before start date");
            }
            if (latestDiscountDto.getEndAt().isBefore(now)) {
                throw new IllegalArgumentException("Discount end date cannot be in past");
            }
            discount.setEndAt(latestDiscountDto.getEndAt());
        }

        //  updating resume at - ## revise
        if (latestDiscountDto.getDiscountStatus() != OperationalStatus.PAUSED
                && discount.getDiscountStatus() != OperationalStatus.PAUSED
                && latestDiscountDto.getResumeAt() != null) {
            throw new IllegalArgumentException("Discount resume date is allowed only for PAUSED discount");
        }

        //  updating discount status - ## revise
        if (latestDiscountDto.getDiscountStatus() != null) {
            OperationalStatus requestStatus = latestDiscountDto.getDiscountStatus();
            OperationalStatus currentStatus = discount.getDiscountStatus();

            if (currentStatus == OperationalStatus.CANCELLED) {
                throw new IllegalArgumentException("Discount already CANCELLED. Cannot revive");
            }
            if (currentStatus == OperationalStatus.EXPIRED) {
                throw new IllegalArgumentException("Discount already EXPIRED. Cannot revive");
            }
            if (requestStatus != OperationalStatus.ACTIVE
                    && requestStatus != OperationalStatus.INACTIVE
                    && requestStatus != OperationalStatus.PAUSED
                    && requestStatus != OperationalStatus.CANCELLED) {
                throw new IllegalArgumentException("Invalid Discount status");
            }
            if (requestStatus == OperationalStatus.PAUSED) {
                LocalDateTime resumeAt = latestDiscountDto.getResumeAt() != null
                        ? latestDiscountDto.getResumeAt()
                        : discount.getResumeAt();

                if (resumeAt == null) {
                    throw new IllegalArgumentException("Discount PAUSED requires Resume date");
                }
                if (resumeAt.isBefore(now)) {
                    throw new IllegalArgumentException("Discount resume date cannot be in past");
                }
                discount.setResumeAt(resumeAt);
            }

            OperationalStatus finalStatus;

            //  user can select - ACTIVE || INACTIVE || PAUSE || CANCEL
            if (requestStatus == OperationalStatus.INACTIVE || requestStatus == OperationalStatus.PAUSED || requestStatus == OperationalStatus.CANCELLED) {
                finalStatus = requestStatus;
            } else if (discount.getStartAt().isAfter(now)) {
                finalStatus = OperationalStatus.SCHEDULED;
            } else {
                finalStatus = OperationalStatus.ACTIVE;
            }

            discount.setDiscountStatus(finalStatus);
        }

        discountRepo.save(discount);
        return true;
    }


    /*
     *
     *   Delete discount (soft delete)
     *
     */
    public boolean deleteDiscount(UUID discountId) {

        //  fetching discount by ID
        Discount discount = discountRepo.findById(discountId)
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with ID: " + discountId));

        //  soft deleting: set isDeleted to true if not already
        logger.info("Deleting discount with ID: {}", discountId);
        discount.setDeleted(true);
        discount.setDeletedAt(LocalDateTime.now());
        discountRepo.save(discount);
        return true;
    }


    //    ********* Discount mapping *********


    /*
     *
     *   Add discount mapping
     *
     */
    public List<MappedContentDto> addDiscountMapping(DiscountMappingDto mappingDto) {

        //  ## better to use domain-level exception instead of IllegalArgumentException: BadRequestException || ValidationException
        //  ## missing duplicate prevention: if same category/product (which is already mapped) is mapped twice - silently ignored by Set. check before add if needed.

        Discount discount = discountRepo.findById(mappingDto.getDiscountId())
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with ID: " + mappingDto.getDiscountId()));

        if (discount.getDiscountLevel() != mappingDto.getLevel()) {
            throw new IllegalArgumentException("Discount level does not match");
        }

        if (mappingDto.getLevel() == DiscountLevel.CATEGORY
                || mappingDto.getLevel() == DiscountLevel.SUB_CATEGORY) {
            //  ------- Discount level - CATEGORY || SUB_CATEGORY -------
            List<ProductCategory> categories = categoriesRepo.findByCategoryIdIn(mappingDto.getSelectionList());

            if (categories.size() != mappingDto.getSelectionList().size()) {
                throw new IllegalArgumentException("Some categories not found");
            }

            if (mappingDto.getLevel() == DiscountLevel.CATEGORY) {
                if (categories.stream().anyMatch(c -> c.getParentCategory().getCategoryId() != null)) {
                    throw new IllegalArgumentException("Sub-category present in category list");
                }
            }

            if (mappingDto.getLevel() == DiscountLevel.SUB_CATEGORY) {
                if (categories.stream().anyMatch(c -> c.getParentCategory().getCategoryId() == null)) {
                    throw new IllegalArgumentException("Parent category present in sub-category list");
                }
            }

            Set<DiscountCategory> mappedSet = categories.stream()
                    .map(c -> DiscountCategory.builder()
                            .discount(discount)
                            .category(c)
                            .isSubCategory(mappingDto.getLevel() == DiscountLevel.SUB_CATEGORY)
                            .build()
                    ).collect(Collectors.toSet());

            discount.getDiscountCategories().addAll(mappedSet);
//            Discount mappedDiscount = discountRepo.save(discountInDb);

            Set<DiscountCategory> discountCategories = discount.getDiscountCategories();
            if (mappingDto.getLevel() == DiscountLevel.CATEGORY) {
                return discountCategories.stream()
                        .filter(DiscountQueryService::isParentCategory)
                        .map(dc -> DiscountQueryService.toMappedContent(dc.getDiscountCategoryId(), dc.getCategory().getCategoryName()))
                        .toList();
            } else {
                return discountCategories.stream()
                        .filter(DiscountQueryService::isSubCategory)
                        .map(dc -> DiscountQueryService.toMappedContent(dc.getDiscountCategoryId(), dc.getCategory().getCategoryName()))
                        .toList();
            }
//            return !mappedDiscount.getDiscountCategories().isEmpty() ? mappedDiscount.getId() : null;

        } else if (mappingDto.getLevel() == DiscountLevel.PRODUCT) {
            //  ------- Discount level - PRODUCT -------
            List<Product> products = productsRepo.findByProductIdIn(mappingDto.getSelectionList());

            if (products.size() != mappingDto.getSelectionList().size()) {
                throw new IllegalArgumentException("Some products not found");
            }

            Set<DiscountProduct> mappedSet = products.stream()
                    .map(p -> DiscountProduct.builder()
                            .discount(discount)
                            .product(p)
                            .build()
                    ).collect(Collectors.toSet());
            discount.getDiscountProducts().addAll(mappedSet);
//            Discount mappedDiscount = discountRepo.save(discountInDb);

            return discount.getDiscountProducts().stream()
                    .map(dp -> DiscountQueryService.toMappedContent(dp.getDiscountProductId(), dp.getProduct().getProductName()))
                    .toList();

//            return !mappedDiscount.getDiscountProducts().isEmpty() ? mappedDiscount.getId() : null;

        } else if (mappingDto.getLevel() == DiscountLevel.VARIANT) {
            //  ------- Discount level - VARIANT -------
            List<ProductItem> items = itemsRepo.findByItemIdIn(mappingDto.getSelectionList());

            if (items.size() != mappingDto.getSelectionList().size()) {
                throw new IllegalArgumentException("Some product items not found");
            }

            Set<DiscountVariant> mappedSet = items.stream()
                    .map(i -> DiscountVariant.builder()
                            .discount(discount)
                            .productItem(i)
                            .build()
                    ).collect(Collectors.toSet());
            discount.getDiscountVariants().addAll(mappedSet);
//            Discount mappedDiscount = discountRepo.save(discountInDb);

            return discount.getDiscountVariants().stream()
                    .map(DiscountQueryService::mapVariant)
                    .toList();
//            return !mappedDiscount.getDiscountVariants().isEmpty() ? mappedDiscount.getId() : null;

        } else {
            return null;
        }
    }

}
