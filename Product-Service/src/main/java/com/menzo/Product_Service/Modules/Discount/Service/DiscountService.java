package com.menzo.Product_Service.Modules.Discount.Service;

import com.menzo.Product_Service.Modules.Discount.Dto.CreateDiscountDto;
import com.menzo.Product_Service.Modules.Discount.Dto.UpdateDiscountDto;
import com.menzo.Product_Service.Modules.Discount.Entity.Discount;
import com.menzo.Product_Service.Modules.Discount.Enum.CapType;
import com.menzo.Product_Service.Modules.Discount.Enum.DiscountType;
import com.menzo.Product_Service.Modules.Discount.Enum.PromotionStatus;
import com.menzo.Product_Service.Modules.Discount.Repo.DiscountRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    @Autowired
    private DiscountRepo discountRepo;


//    ********* ADD, UPDATE, DELETE method *********

    //  Add new discount
    public UUID addNewDiscount(CreateDiscountDto dto) {

        //  data validation
        if (discountRepo.existsByDiscountCode(dto.getDiscountCode())) {
            logger.warn("Duplicate discount code: Discount already exists with code {}", dto.getDiscountCode());
            throw new IllegalArgumentException("Invalid code: Discount already exists with code: " + dto.getDiscountCode());
        }
        if (dto.getType() == DiscountType.PERCENT && (dto.getValue().compareTo(BigDecimal.valueOf(100)) > 0)) {
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
        PromotionStatus status;

        if (dto.getDiscountStatus() == PromotionStatus.INACTIVE) {
            status = PromotionStatus.INACTIVE;
        } else if (dto.getStartAt().isAfter(now)) {
            status = PromotionStatus.SCHEDULED;
        } else if (dto.getStartAt().isBefore(now) && dto.getEndAt().isAfter(now)) {
            status = PromotionStatus.ACTIVE;
        } else {
            status = PromotionStatus.INACTIVE;
        }

        //  saving discount
        Discount discount = Discount.builder()
                .discountCode(dto.getDiscountCode())
                .discountName(dto.getDiscountName())
                .discountDescription(dto.getDiscountDescription())
                .level(dto.getLevel())
                .type(dto.getType())
                .value(dto.getValue())
                .capType(dto.getCapType())
                .capValue(dto.getCapValue())
                .priority(dto.getPriority())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .discountStatus(status)
                .isDelete(false)
                .build();

        Discount savedDiscount = discountRepo.save(discount);

        return savedDiscount != null ? savedDiscount.getId() : null;
    }


    //  Update discount (patch/partial update)
    public boolean updateDiscount(UUID discountId, UpdateDiscountDto dto) {
        Discount discountInDb = discountRepo.findById(discountId)
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with ID: " + discountId));

        if (dto.getDiscountName() != null && !dto.getDiscountName().isEmpty()) {
            if (dto.getDiscountName().length() < 5 || dto.getDiscountName().length() > 100) {
                throw new IllegalArgumentException("Discount name must be 5 to 100 characters long");
            }
            discountInDb.setDiscountName(dto.getDiscountName());
        }

        if (dto.getDiscountDescription() != null && !dto.getDiscountDescription().isEmpty()) {
            if (dto.getDiscountDescription().length() > 255) {
                throw new IllegalArgumentException("Discount description must not exceed 255 characters");
            }
            discountInDb.setDiscountDescription(dto.getDiscountDescription());
        }

        if (dto.getValue() != null) {
            if (dto.getValue().compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new IllegalArgumentException("Discount value should be a positive number");
            }
            if (discountInDb.getType() == DiscountType.PERCENT && (dto.getValue().compareTo(BigDecimal.valueOf(100)) > 0)) {
                throw new IllegalArgumentException("Discount value should be less than 100%");
            }
            discountInDb.setValue(dto.getValue());
        }

        if (dto.getCapType() != null) {
            discountInDb.setCapType(dto.getCapType());
        }

        if (dto.getCapValue() != null) {
            if (discountInDb.getCapType() == CapType.NONE) {
                throw new IllegalArgumentException("Discount cap type available for given cap value is NONE");
            }
            if (dto.getCapValue().compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new IllegalArgumentException("Discount cap value should be a positive number");
            }
            if (discountInDb.getCapType() == CapType.PERCENT && (dto.getCapValue().compareTo(BigDecimal.valueOf(100)) > 0)) {
                throw new IllegalArgumentException("Discount cap value should be less than 100%");
            }
            discountInDb.setCapValue(dto.getCapValue());
        }

        if (dto.getPriority() != null) {
            if (dto.getPriority() == 0) {
                throw new IllegalArgumentException("Discount priority should not be less than 0");
            }
            discountInDb.setPriority(dto.getPriority());
        }

        LocalDateTime now = LocalDateTime.now();

        if (dto.getStartAt() != null) {
            if (discountInDb.getStartAt().isBefore(now)) {
                throw new IllegalArgumentException("Discount already activated. Cannot update Start date.");
            }
            if (dto.getStartAt().isAfter(discountInDb.getEndAt())) {
                throw new IllegalArgumentException("Discount start date cannot be after the end date");
            }
            if (dto.getEndAt() != null && dto.getStartAt().isAfter(dto.getEndAt())) {
                throw new IllegalArgumentException("Discount start date cannot be after then new end date");
            }
            if (dto.getStartAt().isBefore(now)) {
                throw new IllegalArgumentException("Discount start date cannot be in past");
            }
            discountInDb.setStartAt(dto.getStartAt());
        }

        if (dto.getEndAt() != null) {
            if (discountInDb.getEndAt().isBefore(now)) {
                throw new IllegalArgumentException("Discount already expired. Cannot update End date.");
            }
            if (dto.getEndAt().isBefore(discountInDb.getStartAt())) {
                throw new IllegalArgumentException("Discount end date cannot be before start date");
            }
            if (dto.getEndAt().isBefore(now)) {
                throw new IllegalArgumentException("Discount end date cannot be in past");
            }
            discountInDb.setEndAt(dto.getEndAt());
        }

        if (dto.getDiscountStatus() != PromotionStatus.PAUSED
                && discountInDb.getDiscountStatus() != PromotionStatus.PAUSED
                && dto.getResumeAt() != null) {
            throw new IllegalArgumentException("Discount resume date is allowed only for PAUSED discount");
        }

        if (dto.getDiscountStatus() != null) {
            PromotionStatus requestStatus = dto.getDiscountStatus();
            PromotionStatus currentStatus = discountInDb.getDiscountStatus();

            if (currentStatus == PromotionStatus.CANCELLED) {
                throw new IllegalArgumentException("Discount already CANCELLED. Cannot revive");
            }
            if (currentStatus == PromotionStatus.EXPIRED) {
                throw new IllegalArgumentException("Discount already EXPIRED. Cannot revive");
            }
            if (requestStatus != PromotionStatus.ACTIVE
                    && requestStatus != PromotionStatus.INACTIVE
                    && requestStatus != PromotionStatus.PAUSED
                    && requestStatus != PromotionStatus.CANCELLED) {
                throw new IllegalArgumentException("Invalid Discount status");
            }
            if (requestStatus == PromotionStatus.PAUSED) {
                LocalDateTime resumeAt = dto.getResumeAt() != null
                        ? dto.getResumeAt()
                        : discountInDb.getResumeAt();

                if (resumeAt == null) {
                    throw new IllegalArgumentException("Discount PAUSED requires Resume date");
                }
                if (resumeAt.isBefore(now)) {
                    throw new IllegalArgumentException("Discount resume date cannot be in past");
                }
                discountInDb.setResumeAt(resumeAt);
            }

            PromotionStatus finalStatus;

            //  user can select - ACTIVE || INACTIVE || PAUSE || CANCEL
            if (requestStatus == PromotionStatus.INACTIVE || requestStatus == PromotionStatus.PAUSED || requestStatus == PromotionStatus.CANCELLED) {
                finalStatus = requestStatus;
            } else if (discountInDb.getStartAt().isAfter(now)) {
                finalStatus = PromotionStatus.SCHEDULED;
            } else {
                finalStatus = PromotionStatus.ACTIVE;
            }

            discountInDb.setDiscountStatus(finalStatus);
        }

        Discount updated = discountRepo.save(discountInDb);

        return updated != null;
    }


    //  Delete discount (soft delete)
    public void softDeleteDiscount(UUID discountId) {
        Discount discountInDb = discountRepo.findById(discountId)
                .orElseThrow(() -> new EntityNotFoundException("Discount not found with ID: " + discountId));

        if (Boolean.TRUE.equals(discountInDb.getIsDelete())) {
            throw new IllegalArgumentException("Discount already deleted");     // ## or use IllegalStateException
        }

        discountInDb.setIsDelete(true);
        discountRepo.save(discountInDb);
    }

}
