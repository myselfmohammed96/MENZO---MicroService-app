package com.menzo.Product_Service.Controller;

import com.menzo.Product_Service.Dto.VariationsDto.CreateVariationDto;
import com.menzo.Product_Service.Dto.VariationsDto.CreateVariationOptionDto;
import com.menzo.Product_Service.Dto.VariationsDto.OptionDto;
import com.menzo.Product_Service.Dto.VariationsDto.VariationDto;
import com.menzo.Product_Service.Entity.Variation;
import com.menzo.Product_Service.Entity.VariationOption;
import com.menzo.Product_Service.Service.VariationsRetrievalService;
import com.menzo.Product_Service.Service.VariationsService;
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
    private VariationsRetrievalService variationsRetrievalService;

    private static final Logger log = LoggerFactory.getLogger(VariationsRestController.class);

//    ********* GET - Controllers *********
//    ********* Variation *********

    //  Get all Variation with their options
    @GetMapping("/get-all")
    public List<?> getAllVariations() {
        return variationsRetrievalService.getAllVariationsWithOptions();
    }

    //  Get all variations and options by sub-category id
    @GetMapping("get-variations")
    public List<?> getAllVariationsBySub(@RequestParam("id") Long subCategoryId) {
        return variationsRetrievalService.getAllVariationsWithOptionsBySub(subCategoryId);
    }

    //  Get all variations with ids
    @GetMapping("get-all-variations")
    public List<?> getVariationById(){
        return variationsRetrievalService.getAllVariations();
    }

//    ********* Variation options *********

    //public void getAllOptionsByVariationId(){}

    //public void getOptionById(){}

//    ********* POST, PUT, DELETE - Controllers *********
//    ********* Variations *********

    //  Add new Variation
    @PostMapping("/add-variation")
    public ResponseEntity<?> addVariation(@Valid @RequestBody CreateVariationDto newVariation, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));
            log.warn("Validation failed for new variation: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }
        Variation savedVariation = variationsService.addNewVariation(newVariation);
        if (savedVariation != null) {
            log.info("Variation created successfully with ID: {}", savedVariation.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Variation created successfully", "variationId", savedVariation.getId()));
        } else {
            log.error("Variation creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation creation failed."));
        }
    }

    //  Update Variation by id
    @PutMapping("/update-variation")
    public ResponseEntity<?> updateVariation(@RequestParam("id") Long variationId, @RequestBody VariationDto latestVariation) {
        if (variationId == null || variationId <= 0) {
            log.warn("Invalid variation ID: {}", variationId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid Variation ID"));
        }
        Variation updatedVariation = variationsService.updateVariation(variationId, latestVariation);
        if (updatedVariation != null) {
            log.info("Variation with ID {} updated successfully", variationId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation updated successfully", "variationId", updatedVariation.getId()));
        } else {
            log.error("Variation update failed for ID {}", variationId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update variation"));
        }
    }

    //  Delete Variation by id
    @DeleteMapping("/delete-variation")
    public ResponseEntity<?> deleteVariation(@RequestParam("id") Long variationId) {
        if (variationId == null || variationId <= 0) {
            log.warn("Invalid variation ID: {}", variationId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid variation ID"));
        }
        boolean deleted = variationsService.deleteVariation(variationId);
        if (deleted) {
            log.info("Variation with ID {} deleted successfully", variationId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation deleted successfully"));
        } else {
            log.error("Variation deletion failed for ID {}", variationId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation deletion failed"));
        }
    }

//    ********* Variation options *********

    //  Add new variation option
    @PostMapping("/add-option")
    public ResponseEntity<?> addOption(@Valid @RequestBody CreateVariationOptionDto newOption, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));
            log.warn("Validation failed for new variation option: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }
        VariationOption savedOption = variationsService.addNewOption(newOption);
        if (savedOption != null) {
            log.info("Variation option created successfully with ID: {}", savedOption.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Variation option created successfully.", "optionId", savedOption.getId()));
        } else {
            log.error("Variation option creation failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation option creation failed."));
        }
    }

    //  Update variation option by id
    @PutMapping("/update-option")
    public ResponseEntity<?> updateOption(@RequestParam("id") Long optionId, @RequestBody OptionDto latestVariationOption){
        if (optionId == null || optionId <= 0) {
            log.warn("Invalid variation option ID: {}", optionId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid variation option ID"));
        }
        VariationOption updatedOption = variationsService.updateOption(optionId, latestVariationOption);
        if(updatedOption != null) {
            log.info("Variation option with ID {} updated successfully", optionId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation option updated successfully", "optionId", updatedOption.getId()));
        } else {
            log.error("Variation option update failed for ID {}", optionId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation option updation failed"));
        }
    }

    //  Delete variation option by id
    @DeleteMapping("/delete-option")
    public ResponseEntity<?> deleteOption(@RequestParam("id") Long optionId){
        if (optionId == null || optionId <= 0) {
            log.warn("Invalid variation option ID: {}", optionId);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid variation option ID"));
        }
        boolean deleted = variationsService.deleteOption(optionId);
        if (deleted) {
            log.info("Variation option with ID {} deleted successfully", optionId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("message", "Variation option deleted successfully"));
        } else {
            log.error("Variation option deletion failed for ID {}", optionId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Variation option deletion failed"));
        }
    }

//    @GetMapping("/hello")
//    public List<String> hello(@RequestParam("name") String variationName) {
//        return variationsRetrievalService.getOptionsByVariationName(variationName);
//    }
}
