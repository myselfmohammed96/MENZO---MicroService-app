package com.menzo.Product_Service.Variation.Controller;

import com.menzo.Product_Service.Variation.Dto.OptionDto;
import com.menzo.Product_Service.Variation.Service.OptionQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/option")
public class OptionQueryRestController {

    private static final Logger logger = LoggerFactory.getLogger(OptionQueryRestController.class);

    @Autowired
    private OptionQueryService optionQueryService;


    /*
     *
     *   Get options by variation ID
     *
     */
    @GetMapping("get-options")
    public ResponseEntity<List<OptionDto>> getOptionsByVariationId(@RequestParam("id") Long variationId) {
        List<OptionDto> options = optionQueryService.getOptionsByVariationId(variationId);
        return ResponseEntity.ok(options);
    }

}