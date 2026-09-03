package com.menzo.Product_Service.Discount.Controller;

import com.menzo.Product_Service.Discount.Dto.*;
import com.menzo.Product_Service.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Discount.Enum.DiscountStatusTarget;
import com.menzo.Product_Service.Discount.Enum.EnumDto;
import com.menzo.Product_Service.Discount.Service.DiscountQueryService;
import com.menzo.Product_Service.Discount.Service.DiscountCommandService;
import com.menzo.Product_Service.SearchAndFilter.Dto.RequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.function.array.ArrayViaElementArgumentReturnTypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
@Validated  //  for @Min
public class DiscountCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(DiscountCommandRestController.class);

    @Autowired
    private final DiscountCommandService discountService;


    //  ********* Discount APIs *********


    /*
     *
     *   Add new discount
     *
     */
    @PostMapping
    public ResponseEntity<?> addNewDiscount(@Valid @RequestBody CreateDiscountDto newDiscount,
                                            BindingResult result) {
        //  input validation
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            logger.warn("Validation failed for new discount: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        //  adding new discount
        UUID discountId = discountService.addNewDiscount(newDiscount);

        //  response building
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


    /*
     *
     *   Update discount
     *   (patch/partial update)
     *
     */
    @PatchMapping
    public ResponseEntity<?> updateDiscount(@RequestHeader("roles") String roles,
                                            @RequestParam("id") UUID discountId,
                                            @RequestBody UpdateDiscountDto latestDiscount) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (latestDiscount == null) {
                throw new IllegalArgumentException("Invalid input: update body cannot be null");
            }

            //  updating
            boolean isUpdated = discountService.updateDiscount(
                    discountId,
                    latestDiscount
            );

            //  response building
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
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Delete discount (soft delete)
     *
     */
    @DeleteMapping
    public ResponseEntity<?> deleteDiscount(@RequestHeader("roles") String roles,
                                            @RequestParam("id") UUID discountId) {
        if (roles.equals("ADMIN")) {

            //  deleting discount
            boolean deleted = discountService.deleteDiscount(discountId);

            //  response building
            if (deleted) {
                logger.info("Discount with ID '{}' deleted successfully", discountId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Discount deleted successfully"));
            } else {
                logger.error("Discount deletion failed for ID: {}", discountId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Discount deletion failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    //  ********* Discount mapping endpoints *********


    /*
     *
     *   Add discount mapping
     *   Maps discount to applicable category / sub-category / product / variant (item)
     *
     */
    @PostMapping("/mapping")
    public ResponseEntity<?> addDiscountMapping(@RequestHeader("roles") String roles,
                                                @Valid @RequestBody DiscountMappingDto mappingDto,
                                                BindingResult result) {
        //  input validation
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors()
                    .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            logger.warn("Validation failed for discount mapping: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        //  adding discount mapping
        List<MappedContentDto> mappedContent = discountService.addDiscountMapping(mappingDto);

        //  response building - return newly mapped elements with MappedContentDto
        Map<String, Object> responseBody = new HashMap<>();

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
     *
     *   Update discount mapping
     *
     */



    /*
     *
     *   Remove discount mapping
     *
     */

}
