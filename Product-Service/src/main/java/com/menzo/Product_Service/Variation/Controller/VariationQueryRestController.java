package com.menzo.Product_Service.Variation.Controller;

import com.menzo.Product_Service.Enum.Components;
import com.menzo.Product_Service.Variation.Dto.OptionDto;
import com.menzo.Product_Service.Variation.Dto.VariationOptionsMinimalDto;
import com.menzo.Product_Service.Variation.Dto.VariationWithOptionsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/variation")
public class VariationQueryRestController {

    private static final Logger logger = LoggerFactory.getLogger(VariationQueryRestController.class);


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
