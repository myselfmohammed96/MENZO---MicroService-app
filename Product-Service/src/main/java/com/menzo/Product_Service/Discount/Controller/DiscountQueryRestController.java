package com.menzo.Product_Service.Discount.Controller;

import com.menzo.Product_Service.Discount.Dto.DiscountCodeDto;
import com.menzo.Product_Service.Discount.Dto.DiscountSummaryDto;
import com.menzo.Product_Service.Discount.Dto.LevelDetailsDto;
import com.menzo.Product_Service.Discount.Dto.MappedContentDto;
import com.menzo.Product_Service.Discount.Enum.DiscountLevel;
import com.menzo.Product_Service.Discount.Enum.DiscountStatusTarget;
import com.menzo.Product_Service.Discount.Enum.EnumDto;
import com.menzo.Product_Service.Discount.Service.DiscountQueryService;
import com.menzo.Product_Service.SearchAndFilter.Dto.RequestDto;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/discount")
public class DiscountQueryRestController {

    private static final Logger logger = LoggerFactory.getLogger(DiscountQueryRestController.class);

    @Autowired
    private DiscountQueryService discountQueryService;


    //  ********* Discount APIs *********


    /*
     *
     *   Get discount listing
     *
     */
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


    /*
     *
     *   Get discount summary
     *
     */
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


    /*
     *
     *   Existence check - is discount code exists
     *   Used while generating new codes - for add discount form
     *
     */
    @PostMapping("/code-exist")
    public ResponseEntity<?> checkDiscountCodeExist(@RequestBody DiscountCodeDto code) {
        boolean exists = discountQueryService.checkDiscountCodeExist(code);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("exists", exists));
    }


    //  ********* Mapping endpoints *********


    /*
     *
     *   Get discount mapped content
     *
     */
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


    /*
     *
     *   Get mapping level elements
     *
     */
    @GetMapping("/level-elements")
    public ResponseEntity<?> getMappingLevelElements(@RequestParam("cLevel") DiscountLevel currentLevel,
                                                     @RequestParam(name = "previousId", required = false) UUID id) {

        List<LevelDetailsDto> levelDetails = discountQueryService.getLevelDetails(
                id,
                currentLevel
        );
        return ResponseEntity.ok(levelDetails);
    }


    //  ********* Enum data endpoints *********


    /*
     *
     *   Get discount level
     *
     */
    @GetMapping("/level")
    public ResponseEntity<EnumDto> getDiscountLevel() {
        EnumDto levels = discountQueryService.getDiscountLevel();
        return ResponseEntity.ok(levels);
    }


    /*
     *
     *   Get discount type
     *
     */
    @GetMapping("/type")
    public ResponseEntity<EnumDto> getDiscountType() {
        EnumDto types = discountQueryService.getDiscountType();
        return ResponseEntity.ok(types);
    }


    /*
     *
     *   Get cap type
     *
     */
    @GetMapping("/cap-type")
    public ResponseEntity<EnumDto> getCapType() {
        EnumDto capType = discountQueryService.getCapType();
        return ResponseEntity.ok(capType);
    }


    /*
     *
     *   Get discount status
     *   Used for add discount form
     *
     */
    @GetMapping("/form-status")
    public ResponseEntity<EnumDto> getDiscountFormStatus() {
        EnumDto status = discountQueryService.getDiscountStatus(DiscountStatusTarget.FORM);
        return ResponseEntity.ok(status);
    }


    /*
     *
     *   Get discount status
     *   Used for discount summary
     *
     */
    @GetMapping("/summary-status")
    public ResponseEntity<EnumDto> getDiscountSummaryStatus() {
        EnumDto status = discountQueryService.getDiscountStatus(DiscountStatusTarget.SUMMARY);
        return ResponseEntity.ok(status);
    }

}
