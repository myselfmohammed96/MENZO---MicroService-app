package com.menzo.Product_Service.Modules.Variation.Controller;

import com.menzo.Product_Service.Enum.Components;
import com.menzo.Product_Service.Modules.Variation.Entity.Variation;
import com.menzo.Product_Service.Modules.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Modules.Variation.Service.VariationsQueryService;
import com.menzo.Product_Service.Modules.Variation.Dto.*;
import com.menzo.Product_Service.Modules.Variation.Service.VariationsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/variations")
public class VariationsRestController {

    @Autowired
    private VariationsService variationsService;

    @Autowired
    private VariationsQueryService variationsRetrievalService;

    private static final Logger logger = LoggerFactory.getLogger(VariationsRestController.class);



//    ********* GET - Controllers *********
///    ********* Variation *********

    //  Get all Variation with their options
    //  TESTED - with Postman
//    @GetMapping("/get-all")
//    public List<?> getAllVariations() {
//        return variationsRetrievalService.getAllVariationsWithOptions();
//    }

    //  Get all variations and options by sub-category id
    //  TESTED - with Postman

    @GetMapping("get-variations")
    public List<VariationWithOptionsDto> getAllVariationsBySub(@RequestParam("id") Long subCategoryId) {
        return variationsRetrievalService.getAllVariationsWithOptionsByCategory(
                Components.SUB_CATEGORY,
                subCategoryId
        );
    }

    //  get all variations without options
    //  TESTED - with Postman
    //  used - admin-service
    @GetMapping("get-all-variations")
    public List<?> getAllVariation(){
        return variationsRetrievalService.getAllVariations();
    }



//    ********* Options *********

    //  get options by variation id - admin-service
    @GetMapping("get-options")
    public List<OptionDto> getOptionsByVariationId(@RequestParam("id") Long variationId) {
        return variationsRetrievalService.getOptionsByVariationId(variationId);
    }



//    ********* POST, PUT, DELETE - Controllers *********
//    ********* Variations *********

    //  Add new Variation - admin-service
    @PostMapping("/add-variation")
    public ResponseEntity<?> addVariation(
            @Valid @RequestBody CreateVariationDto newVariation,
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
        Variation savedVariation = variationsService.addNewVariation(newVariation);

        //  response
        if (savedVariation != null) {
            logger.info("Variation created successfully with ID: {}", savedVariation.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Variation created successfully", "variationId", savedVariation.getId()));
        } else {
            logger.error("Variation creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation creation failed."));
        }
    }


    //  Update Variation by id - admin-service
    @PutMapping("/update-variation")
    public ResponseEntity<?> updateVariation(
            @RequestParam("id") Long variationId,
            @RequestBody VariationDto latestVariation) {

        //  input validation
        if (variationId == null || variationId <= 0) {
            logger.warn("Invalid variation ID: {}", variationId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid Variation ID"));
        }

        //  updating variation
        Variation updatedVariation = variationsService.updateVariation(variationId, latestVariation);

        //  response
        if (updatedVariation != null) {
            logger.info("Variation with ID {} updated successfully", variationId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation updated successfully", "variationId", updatedVariation.getId()));
        } else {
            logger.error("Variation update failed for ID {}", variationId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update variation"));
        }
    }


    //  Delete Variation by id - admin-service
    @DeleteMapping("/delete-variation")
    public ResponseEntity<?> deleteVariation(@RequestParam("id") Long variationId) {

        //  input validation
        if (variationId == null || variationId <= 0) {
            logger.warn("Invalid variation ID: {}", variationId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid variation ID"));
        }

        //  deleting variation
        boolean deleted = variationsService.deleteVariation(variationId);

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



//    ********* Variation options *********

    //  Add new variation option - admin-service
    @PostMapping("/add-option")
    public ResponseEntity<?> addOption(
            @Valid @RequestBody CreateVariationOptionDto newOption,
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
        VariationOption savedOption = variationsService.addNewOption(newOption);

        // response
        if (savedOption != null) {
            logger.info("Variation option created successfully with ID: {}", savedOption.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            Map.of("message", "Variation option created successfully.",
                                    "optionId", savedOption.getId())
                    );
        } else {
            logger.error("Variation option creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of("message",
                                    "Variation option creation failed.")
                    );
        }
    }


    //  Update variation option by id - admin-service
    @PutMapping("/update-option")
    public ResponseEntity<?> updateOption(
            @RequestParam("id") Long optionId,
            @RequestBody OptionDto latestVariationOption){

        //  input validation
        if (optionId == null || optionId <= 0) {
            logger.warn("Invalid variation option ID: {}", optionId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid variation option ID"));
        }

        //  updating variation option
        VariationOption updatedOption = variationsService.updateOption(optionId, latestVariationOption);

        //  response
        if(updatedOption != null) {
            logger.info("Variation option with ID {} updated successfully", optionId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation option updated successfully", "optionId", updatedOption.getId()));
        } else {
            logger.error("Variation option update failed for ID {}", optionId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation option updation failed"));
        }
    }


    //  Delete variation option by id - admin-service
    @DeleteMapping("/delete-option")
    public ResponseEntity<?> deleteOption(@RequestParam("id") Long optionId){

        //  input validation
        if (optionId == null || optionId <= 0) {
            logger.warn("Invalid variation option ID: {}", optionId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid variation option ID"));
        }

        //  deleting variation option
        boolean deleted = variationsService.deleteOption(optionId);

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









    /*  used in
    * admin-service - product-details-add-item.js
    *
    */
    @GetMapping("/size")
    public ResponseEntity<VariationOptionsMinimalDto> getSizes() {
        VariationOptionsMinimalDto variationOptions = variationsRetrievalService
                .getVariationWithOptionsByVariationName("Size");
        return ResponseEntity.ok(variationOptions);
    }

    /*  used in
     * admin-service - product-details-add-item.js
     *
     */
    @GetMapping("/colors")
    public ResponseEntity<VariationOptionsMinimalDto> getColors() {
        VariationOptionsMinimalDto variationOptions = variationsRetrievalService
                .getVariationWithOptionsByVariationName("Colors");
        return ResponseEntity.ok(variationOptions);
    }

//    @GetMapping("/color-opts")
//    public void getColorOpts() {
//        return;
//    }
}
