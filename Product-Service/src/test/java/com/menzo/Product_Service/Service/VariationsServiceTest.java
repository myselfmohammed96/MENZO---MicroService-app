package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.VariationsDto.CreateVariationOptionDto;
import com.menzo.Product_Service.Entity.VariationOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VariationsServiceTest {

    @Autowired
    private VariationsService variationsService;

    @Test
    public void testAddNewOptionForColor() {
        CreateVariationOptionDto newOption = CreateVariationOptionDto.builder()
                .optionValue("Dark purple")
                .variationId(3L)
                .colorCode("#301934")
                .build();
        VariationOption addedOption = variationsService.addNewOption(newOption);
        System.out.println(addedOption);
    }
}