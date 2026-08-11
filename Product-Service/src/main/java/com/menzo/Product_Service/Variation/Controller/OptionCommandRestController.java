package com.menzo.Product_Service.Variation.Controller;

import com.menzo.Product_Service.Variation.Dto.CreateOptionDto;
import com.menzo.Product_Service.Variation.Dto.OptionDto;
import com.menzo.Product_Service.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Variation.Service.OptionCommandService;
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
@RequestMapping("/option")
public class OptionCommandRestController {

    private static final Logger logger = LoggerFactory.getLogger(OptionCommandRestController.class);

    @Autowired
    private OptionCommandService optionCommandService;


    /*
     *
     *   Add new Variation option
     *
     */
    @PostMapping("/add")
    public ResponseEntity<?> addOption(@Valid @RequestBody CreateOptionDto newOption,
                                       BindingResult result) {

        // input validation
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));
            logger.warn("Validation failed for new variation option: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        // saving option
        VariationOption savedOption = optionCommandService.addNewOption(newOption);

        // response
        if (savedOption != null) {
            logger.info("Variation option created successfully with ID: {}", savedOption.getOptionId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Variation option created successfully.", "optionId", savedOption.getOptionId()));
        } else {
            logger.error("Variation option creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation option creation failed."));
        }
    }


    /*
     *
     *   Update variation option
     *   Variation option identified by option ID
     *
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateOption(@RequestParam("id") UUID optionId,
                                          @RequestBody OptionDto latestVariationOption) {

        //  input validation
        if (optionId == null) {
            logger.warn("Invalid variation option ID: {}", optionId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid variation option ID"));
        }

        //  updating variation option
        VariationOption updatedOption = optionCommandService.updateOption(optionId, latestVariationOption);

        //  response
        if (updatedOption != null) {
            logger.info("Variation option with ID {} updated successfully", optionId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation option updated successfully", "optionId", updatedOption.getOptionId()));
        } else {
            logger.error("Variation option update failed for ID {}", optionId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation option updation failed"));
        }
    }


    /*
     *
     *   Update variation option active status
     *
     */
    @PutMapping("/update-status")
    public ResponseEntity<?> updateOptionActiveStatus(@RequestHeader("roles") String roles,
                                                      @RequestParam("id") UUID optionId,
                                                      @RequestParam("active") boolean isActive) {
        if (roles.equals("ADMIN")) {

            //  input validation
            if (optionId == null) {
                logger.warn("Invalid variation option ID: {}", optionId);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid variation option ID"));
            }

            //  update active status
            boolean updatedActive = optionCommandService.updateOptionActiveStatus(optionId, isActive);

            //  response
            if (isActive == updatedActive) {
                logger.info("Active status for variation option {}, updated successfully", optionId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(Map.of("message", "Variation option active status updated successfully"));
            } else {
                logger.error("Active status update failed for variation option ID: {}", optionId);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Variation option active status update failed."));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    /*
     *
     *   Delete variation option
     *   Variation option identified by option ID
     *
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOption(@RequestParam("id") UUID optionId) {

        //  input validation
        if (optionId == null) {
            logger.warn("Invalid variation option ID: {}", optionId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid variation option ID"));
        }

        //  deleting variation option
        boolean deleted = optionCommandService.deleteOption(optionId);

        //  response
        if (deleted) {
            logger.info("Variation option with ID {} deleted successfully", optionId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation option deleted successfully"));
        } else {
            logger.error("Variation option deletion failed for ID {}", optionId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation option deletion failed"));
        }
    }
}
