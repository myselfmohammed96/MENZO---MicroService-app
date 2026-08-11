package com.menzo.Product_Service.Variation.Controller;

import com.menzo.Product_Service.Variation.Dto.*;
import com.menzo.Product_Service.Variation.Entity.Variation;
import com.menzo.Product_Service.Variation.Service.VariationCommandService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/variation")
public class VariationCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(VariationCommandRestController.class);

    @Autowired
    private VariationCommandService variationCommandService;


    /*
     *
     *   Add new variation
     *
     */
    @PostMapping("/add")
    public ResponseEntity<?> addVariation(@Valid @RequestBody CreateVariationDto newVariation,
                                          BindingResult result) {
        //  input validation
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));
            logger.warn("Validation failed for new variation: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        //  adding variation
        Variation savedVariation = variationCommandService.addNewVariation(newVariation);

        //  response
        if (savedVariation != null) {
            logger.info("Variation created successfully with ID: {}", savedVariation.getVariationId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Variation created successfully", "variationId", savedVariation.getVariationId()));
        } else {
            logger.error("Variation creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation creation failed."));
        }
    }


    /*
     *
     *   Update variation
     *   Variation identified by variation ID
     *
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateVariation(@RequestParam("id") UUID variationId,
                                             @RequestBody VariationDto latestVariation) {
        //  input validation
        if (variationId == null) {
            logger.warn("Invalid variation ID: {}", variationId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid Variation ID"));
        }

        //  updating variation
        Variation updatedVariation = variationCommandService.updateVariation(variationId, latestVariation);

        //  response
        if (updatedVariation != null) {
            logger.info("Variation with ID {} updated successfully", variationId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation updated successfully", "variationId", updatedVariation.getVariationId()));
        } else {
            logger.error("Variation update failed for ID {}", variationId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update variation"));
        }
    }


    /*
     *
     *   Update variation active status
     *
     */
    @PutMapping("/update-status")
    public ResponseEntity<?> updateVariationActiveStatus(@RequestHeader("roles") String roles,
                                                         @RequestParam("id") UUID variationId,
                                                         @RequestParam("active") boolean isActive) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (variationId == null) {
                logger.warn("Invalid variation ID: {}", variationId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid variation ID"));
            }

            //  update active status
            boolean updatedActive = variationCommandService.updateVariationActiveStatus(variationId, isActive);

            //  response
            if (isActive == updatedActive) {
                logger.info("Active status for variation {}, updated successfully", variationId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Variation active status updated successfully"));
            } else {
                logger.error("Active status update failed for variation ID: {}", variationId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Variation active status update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Delete variation
     *   Variation identified by variation ID
     *
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteVariation(@RequestParam("id") UUID variationId) {

        //  input validation
        if (variationId == null) {
            logger.warn("Invalid variation ID: {}", variationId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid variation ID"));
        }

        //  deleting variation
        boolean deleted = variationCommandService.deleteVariation(variationId);

        //  response
        if (deleted) {
            logger.info("Variation with ID {} deleted successfully", variationId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation deleted successfully"));
        } else {
            logger.error("Variation deletion failed for ID {}", variationId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation deletion failed"));
        }
    }

}
