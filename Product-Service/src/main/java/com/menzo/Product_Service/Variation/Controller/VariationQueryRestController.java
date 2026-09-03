package com.menzo.Product_Service.Variation.Controller;

import com.menzo.Product_Service.Enum.Components;
import com.menzo.Product_Service.Variation.Dto.VariationDto;
import com.menzo.Product_Service.Variation.Dto.VariationOptionsDto;
import com.menzo.Product_Service.Variation.Service.VariationQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/variation")
public class VariationQueryRestController {

    private static final Logger logger = LoggerFactory.getLogger(VariationQueryRestController.class);

    @Autowired
    private VariationQueryService variationQueryService;


    /*
     *
     *   Get all variations
     *   With variation options
     *
     */
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllVariationsWithOptions() {
        List<VariationOptionsDto> variations = variationQueryService.getAllVariationsWithOptions();
        return ResponseEntity.ok(variations);
    }


    /*
     *
     *   Get all variations
     *   Without variation options
     *
     */
    @GetMapping("get-all-variations")
    public ResponseEntity<List<?>> getAllVariationWithoutOptions() {
        List<VariationDto> variations = variationQueryService.getAllVariationsWithoutOptions();
        return ResponseEntity.ok(variations);
    }


    /*
     *
     *   Get all variations and options
     *   Associated with sub-category
     *   Identified by sub-category ID
     *
     */
    @GetMapping("get-variations")
    public ResponseEntity<List<VariationOptionsDto>> getAllVariationsWithOptionsBySub(@RequestParam("id") UUID subCategoryId) {
        List<VariationOptionsDto> variations = variationQueryService.getAllVariationsWithOptionsByCategory(
                Components.SUB_CATEGORY,
                subCategoryId
        );
        return ResponseEntity.ok(variations);
    }


    /*
     *
     *  Get all sizes
     *
     */
    @GetMapping("/sizes")
    public ResponseEntity<VariationOptionsDto> getSizes() {
        VariationOptionsDto variationOptions = variationQueryService
                .getVariationWithOptionsByVariationName("Size");
        return ResponseEntity.ok(variationOptions);
    }


    /*
     *
     *  Get all colors
     *
     */
    @GetMapping("/colors")
    public ResponseEntity<VariationOptionsDto> getColors() {
        VariationOptionsDto variationOptions = variationQueryService
                .getVariationWithOptionsByVariationName("Colors");
        return ResponseEntity.ok(variationOptions);
    }

}







//    @GetMapping("/color-opts")
//    public void getColorOpts() {
//        return;
//    }