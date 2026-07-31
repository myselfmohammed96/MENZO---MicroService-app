package com.menzo.Product_Service.Discount.Controller;

import com.menzo.Product_Service.Discount.Dto.*;
import com.menzo.Product_Service.Modules.Discount.Dto.*;
import com.menzo.Product_Service.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Discount.Enum.DiscountStatusTarget;
import com.menzo.Product_Service.Discount.Enum.EnumDto;
import com.menzo.Product_Service.Discount.Service.DiscountQueryService;
import com.menzo.Product_Service.Discount.Service.DiscountService;
import com.menzo.Product_Service.SearchAndFilter.Dto.RequestDto;
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
import java.util.List;
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


    /*
     *   --------------------------------------
     *   ********* Discount endpoints *********
     *   --------------------------------------
     */

    /// /    ********* FETCH data methods *********

    //  discount listing
    @PostMapping("/listing")
    public ResponseEntity<?> getDiscountListing(@RequestParam(name = "page", defaultValue = "0") @Min(0) Integer page,
                                                @RequestParam(name = "size", defaultValue = "11") @Min(1) Integer size,
                                                @RequestParam(name = "sort", required = false) String sortRequest,
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
                requestDto
        );

        Map<String, Object> responseBody = new HashMap<>();
        if (pageContent.getContent() != null && !pageContent.getContent().isEmpty()) {
            logger.info("Sending discount listing response");
            responseBody.put("message", "Discount listing response");
        } else {
            logger.warn("No discounts found.");
            responseBody.put("message", "No discounts found");
        }
        responseBody.put("pageContent", pageContent);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseBody);
    }

    //  discount summary
    @GetMapping("/summary")
    public ResponseEntity<?> getDiscountSummary(@RequestHeader("roles") String roles,
                                                @RequestParam("id") UUID discountId) {
        DiscountSummaryDto summary = discountQueryService.getDiscountSummary(discountId);

        Map<String, Object> responseBody = new HashMap<>();
        if (summary != null) {
            logger.info("Sending discount summary response");
            responseBody.put("message", "Discount summary response");
            responseBody.put("summary", summary);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            logger.warn("Error sending discount summary for ID: {}", discountId);
            responseBody.put("message", "Error fetching discount summary.");
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


    /// /    ********* Existence check methods *********

    //  Discount code - existence check (while generating new codes for add discount form)
    @PostMapping("/check-code-exist")
    public ResponseEntity<?> checkDiscountCodeExist(@RequestBody DiscountCodeDto code) {
        boolean exists = discountQueryService.checkDiscountCodeExist(code);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("exists", exists));
    }


    /*
     *   -------------------------------------
     *   ********* Mapping endpoints *********
     *   -------------------------------------
     */

    /// /    ********* FETCH data methods *********

    //  discount mapped content
    @PostMapping("/mapped-content")
    public ResponseEntity<?> getDiscountMappedContent(@RequestHeader("roles") String roles,
                                                      @RequestParam("id") UUID discountId,
                                                      @RequestParam(name = "sort", required = false) String sortRequest,
                                                      @RequestBody(required = false) RequestDto requestDto) {
        if (sortRequest != null && sortRequest.isEmpty()) {
            throw new IllegalArgumentException("Sort request cannot be empty");
        }
        if (requestDto != null && requestDto.getFilterRequestDtos().isEmpty()) {
            throw new IllegalArgumentException("Filter request cannot be empty");
        }

        List<MappedContentDto> content = discountQueryService.getDiscountMappedContent(
                discountId,
                sortRequest != null ? sortRequest : "",
                requestDto
        );

        Map<String, Object> responseBody = new HashMap<>();
        if (content != null) {
            if (content.isEmpty()) {
                logger.info("Sending discount mapped content response, with empty list.");
                responseBody.put("message", "No mapped content found.");
            } else {
                logger.info("Sending discount mapped content response.");
                responseBody.put("message", "Discount mapped content response.");
            }
            responseBody.put("content", content);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            logger.warn("Error sending discount mapped content for discount ID: {}", discountId);
            responseBody.put("message", "Error fetching discount mapped content.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    //  mapping level elements
    @GetMapping("/level-elements")
    public ResponseEntity<?> getMappingLevelElements(@RequestParam("cLevel") DiscountLevel currentLevel,
                                             @RequestParam(name = "previousId", required = false) Long id) {

        List<LevelDetailsDto> levelDetails = discountQueryService.getLevelDetails(
                id,
                currentLevel
        );
        return ResponseEntity.ok(levelDetails);
    }


    /// /   ********* POST, PUT, DELETE methods *********

    //  Discount mapping
    @PostMapping("/mapping")
    public ResponseEntity<?> addDiscountMapping(@RequestHeader("roles") String roles,
                                                @Valid @RequestBody DiscountMappingDto mappingDto,
                                                BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            logger.warn("Validation failed for discount mapping: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

//        UUID mappedDiscountId = discountService.discountMapping(mappingDto);
        List<MappedContentDto> mappedContent = discountService.addDiscountMapping(mappingDto);

        //  ## return newly mapped elements with MappedContentDto

        Map<String, Object> responseBody = new HashMap<>();
//        if (mappedDiscountId != null) {
//            logger.info("Discount mapped successfully");
//            responseBody.put("message", "Discount mapped successfully");
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(responseBody);
//        } else {
//            logger.warn("Discount mapping failed");
//            responseBody.put("message", "Discount mapping failed");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(responseBody);
//        }
        if (mappedContent != null) {
            logger.info("Discount mapped successfully");
            responseBody.put("message", "Discount mapped successfully");
            responseBody.put("mappedContent", mappedContent);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            logger.warn("Discount mapping failed");
            responseBody.put("message", "Discount mapping failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
    }


    /*
     *   ---------------------------------------
     *   ********* Enum data endpoints *********
     *   ---------------------------------------
     */

    //  get Discount level
    @GetMapping("/level")
    public ResponseEntity<EnumDto> getDiscountLevel() {
        EnumDto levels = discountQueryService.getDiscountLevel();
        return ResponseEntity.ok(levels);
    }

    //  get Discount type
    @GetMapping("/type")
    public ResponseEntity<EnumDto> getDiscountType() {
        EnumDto types = discountQueryService.getDiscountType();
        return ResponseEntity.ok(types);
    }

    //  get Cap type
    @GetMapping("/cap-type")
    public ResponseEntity<EnumDto> getCapType() {
        EnumDto capType = discountQueryService.getCapType();
        return ResponseEntity.ok(capType);
    }

    //  get Discount status for add Discount form
    @GetMapping("/form-status")
    public ResponseEntity<EnumDto> getDiscountFormStatus() {
        EnumDto status = discountQueryService.getDiscountStatus(DiscountStatusTarget.FORM);
        return ResponseEntity.ok(status);
    }

    //  get Discount status for discount summary
    @GetMapping("/summary-status")
    public ResponseEntity<EnumDto> getDiscountSummaryStatus() {
        EnumDto status = discountQueryService.getDiscountStatus(DiscountStatusTarget.SUMMARY);
        return ResponseEntity.ok(status);
    }


}
