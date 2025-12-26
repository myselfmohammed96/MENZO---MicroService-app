package com.menzo.Product_Service.Modules.Discount.Controller;

import com.menzo.Product_Service.Modules.Discount.Dto.CreateDiscountDto;
import com.menzo.Product_Service.Modules.Discount.Dto.DiscountMappingDto;
import com.menzo.Product_Service.Modules.Discount.Dto.UpdateDiscountDto;
import com.menzo.Product_Service.Modules.Discount.Service.DiscountQueryService;
import com.menzo.Product_Service.Modules.Discount.Service.DiscountService;
import com.menzo.Product_Service.Modules.SearchAndFilter.Dto.RequestDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
@Validated  //  for @Min
public class DiscountRestController {

    private static final Logger logger = LoggerFactory.getLogger(DiscountRestController.class);

    @Autowired
    private final DiscountService discountService;

    @Autowired
    private final DiscountQueryService discountQueryService;


    /// /    ********* FETCH data methods *********

    @PostMapping("/listing")
    public ResponseEntity<?> getDiscountListing(@RequestParam(defaultValue = "0") @Min(0) Integer page,
                                                @RequestParam(defaultValue = "11") @Min(1) Integer size,
                                                @RequestParam(required = false) String sortRequest,
                                                @RequestBody(required = false) RequestDto requestDto) {

        if (sortRequest != null && sortRequest.isEmpty()) {
            throw new IllegalArgumentException("Sort request cannot be empty");
        }
        if (requestDto != null && requestDto.getFilterRequestDtos().isEmpty()) {
            throw new IllegalArgumentException("Filter request cannot be empty");
        }

        Page<?> pageContent = discountQueryService.getDiscountListing(
                page,
                size,
                sortRequest != null ? sortRequest : "",
                requestDto != null ? requestDto : null
        );

        Map<String, Object> responseBody = new HashMap<>();
        if (pageContent.getContent() != null && !pageContent.getContent().isEmpty()) {
            logger.info("Sending discount listing response successfully");
            responseBody.put("message", "Discount listing response");
            responseBody.put("pageContent", pageContent);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            logger.warn("Sending discount listing response failed");
            responseBody.put("message", "Error getting discount listing");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    /// /    ********* POST, PUT, DELETE methods *********

    //  Add new discount
    @PostMapping
    public ResponseEntity<?> addNewDiscount(@Valid @RequestBody CreateDiscountDto newDiscount,
                                            BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            logger.warn("Validation failed for new discount: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        UUID discountId = discountService.addNewDiscount(newDiscount);

        Map<String, Object> responseBody = new HashMap<>();
        if (discountId != null) {
            logger.info("Discount created successfully with ID: {}", discountId);
            responseBody.put("message", "Discount created successfully");
            responseBody.put("discountId", discountId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(responseBody);
        } else {
            logger.warn("Discount creation failed");
            responseBody.put("message", "Discount creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    //  Update discount (patch/partial update)
    @PatchMapping
    public ResponseEntity<?> editDiscount(@RequestHeader("roles") String roles,
                                          @RequestParam("id") UUID discountId,
                                          @RequestBody UpdateDiscountDto latestDiscount) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Invalid user roles");
        }
        if (latestDiscount == null) {
            throw new IllegalArgumentException("Invalid input: update body cannot be null");
        }

        boolean isUpdated = discountService.updateDiscount(
                discountId,
                latestDiscount
        );

        Map<String, Object> responseBody = new HashMap<>();
        if (isUpdated) {
            logger.info("Discount updated successfully");
            responseBody.put("message", "Discount updated successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            logger.warn("Discount update failed");
            responseBody.put("message", "Discount update failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    //  Delete discount (soft delete)
    @DeleteMapping
    public ResponseEntity<?> deleteDiscount(@RequestHeader("roles") String roles,
                                            @RequestParam("id") UUID discountId) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Invalid user roles");
        }
        discountService.softDeleteDiscount(discountId);

        logger.info("Discount deleted successfully");
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Discount deleted successfully");
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseBody);
    }


    /// /   ********* Discount mapping methods *********

    //  Discount mapping
    @PostMapping("/mapping")
    public ResponseEntity<?> discountMapping(@RequestHeader("roles") String roles,
                                             @Valid @RequestBody DiscountMappingDto mappingDto,
                                             BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            logger.warn("Validation failed for discount mapping: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        UUID mappedDiscountId = discountService.discountMapping(mappingDto);

        Map<String, Object> responseBody = new HashMap<>();
        if (mappedDiscountId != null) {
            logger.info("Discount mapped successfully");
            responseBody.put("message", "Discount mapped successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            logger.warn("Discount mapping failed");
            responseBody.put("message", "Discount mapping failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }

}
