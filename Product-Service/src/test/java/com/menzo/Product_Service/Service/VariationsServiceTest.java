package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.VariationsDto.CreateVariationDto;
import com.menzo.Product_Service.Dto.VariationsDto.CreateVariationOptionDto;
import com.menzo.Product_Service.Entity.Variation;
import com.menzo.Product_Service.Entity.VariationOption;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VariationsServiceTest {

    @Autowired
    private VariationsService variationsService;

    @Test
    public void testAddNewVariation() {
        CreateVariationDto newVariation = CreateVariationDto.builder()
                .variationName("New variation")
                .build();
        Variation variation = variationsService.addNewVariation(newVariation);
        System.out.println(variation);
    }

//    @Test
//    public void testAddNewOptionForColor() {
//        CreateVariationOptionDto newOption = CreateVariationOptionDto.builder()
//                .optionValue("Dark purple")
//                .variationId(3L)
//                .colorCode("#301934")
//                .build();
//        VariationOption addedOption = variationsService.addNewOption(newOption);
//        System.out.println(addedOption);
//    }
}