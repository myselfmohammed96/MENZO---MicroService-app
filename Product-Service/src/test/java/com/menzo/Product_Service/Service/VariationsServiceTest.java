package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.VariationsDto.CreateVariationDto;
import com.menzo.Product_Service.Dto.VariationsDto.CreateVariationOptionDto;
import com.menzo.Product_Service.Dto.VariationsDto.OptionDto;
import com.menzo.Product_Service.Dto.VariationsDto.VariationDto;
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



//    ********* Variations *********
    @Test
    public void testAddNewVariation() {
        CreateVariationDto newVariation = CreateVariationDto.builder()
                .variationName("New variation")
                .build();
        Variation variation = variationsService.addNewVariation(newVariation);
        System.out.println(variation);
    }

    @Test
    public void testUpdateVariation() {
        VariationDto variationDto = VariationDto.builder()
                .variationName("Variation new")
                .build();
        Variation variation = variationsService.updateVariation(
                25L,
                variationDto
        );
        System.out.println(variation);
    }

    @Test
    public void testDeleteVariation() {
        boolean deleted = variationsService.deleteVariation(25L);
        System.out.println("Deleted: " + deleted);
    }



//    ********* Variation options *********

    @Test
    public void testAddNewOption() {
        CreateVariationOptionDto newOption = CreateVariationOptionDto.builder()
                .optionValue("Peach")
                .colorCode("#FFE5B4")
                .variationId(3L)
                .build();
        VariationOption addedOption = variationsService.addNewOption(newOption);
        System.out.println(addedOption);
    }

    @Test
    public void testUpdateOption() {
        OptionDto option = OptionDto.builder()
                .optionValue("Space grey")
                .colorCode("#343d46")
                .build();
        VariationOption updatedOption = variationsService.updateOption(
                63L,
                option
        );
        System.out.println("Updated: " + updatedOption);
    }

    @Test
    public void testDeleteOption() {
        boolean deleted = variationsService.deleteOption(64L);
        System.out.println("Deleted: " + deleted);
    }

}