package com.menzo.Product_Service.Modules.Variation.Service;

import com.menzo.Product_Service.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Enum.Components;
import com.menzo.Product_Service.Variation.Dto.VariationDto;
import com.menzo.Product_Service.Variation.Dto.VariationOptionsDto;
import com.menzo.Product_Service.Variation.Service.OptionQueryService;
import com.menzo.Product_Service.Variation.Service.VariationQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class VariationsRetrievalServiceTest {

    @Autowired
    private VariationQueryService variationsRetrievalService;

    @Autowired
    private OptionQueryService optionQueryService;

    @Test
    public void testGetAllVariationsWithOptions() {
        List<VariationOptionsDto> variations = variationsRetrievalService.getAllVariationsWithOptions();
        System.out.println(variations);
    }

//    @Test
//    public void testGetAllVariationsWithOptionsBySub() {
//        List<VariationOptionsDto> data = variationsRetrievalService.getAllVariationsWithOptionsByCategory(
//                Components.CATEGORY,
//                2L
//        );
//        data.stream().forEach(d -> System.out.println(d));
//        System.out.println(data.size());
//    }

//    @Test
//    public void testGetAllVariations() {
//        List<VariationDto> variationsList = variationsRetrievalService.getAllVariations();
//        System.out.println(variationsList);
//    }

//    @Test
//    public void testGetOptionsByVariationName() {
//        List<String> options = variationsRetrievalService.getOptionsByVariationName(
//                null,
//                "Size"
//        );
//        System.out.println(options);
//    }

//    @Test
//    public void testGetSizes() {
//        VariationOptionsMinimalDto sizes = variationsRetrievalService
//                .getVariationWithOptionsByVariationName("Size");
//        System.out.println(sizes);
//    }

//    @Test
//    @Transactional
//    public void testGetOptionsByIds() {
//        List<VariationOption> options = optionQueryService.getOptionsByIds(Arrays.asList(
//                3L,
//                4L,
//                5L,
//                7L,
//                8L
//        ));
//        System.out.println(options);
//    }

//    @Test
//    public void testGetOptionById() {
//        VariationOption option = variationsRetrievalService.getOptionById(3L);
//        System.out.println(option);
//    }

//    @Test
//    public void testGetOptionsByVariationId() {
//        List<OptionDto> options = variationsRetrievalService
//                .getOptionsByVariationId(9L);
//        System.out.println(options);
//        System.out.println(options.size());
//    }

//    @Test
//    public void testGetColorOptionsByVariationId() {
//        List<OptionDto> options = variationsRetrievalService
//                .getOptionsByVariationId(3L);
//        System.out.println(options);
//        System.out.println(options.size());
//    }

//    @Test
//    public void testGetOptionIdsByVariation() {
//        List<Long> options = variationsRetrievalService.getOptionIdsByVariation("Colors");
//        System.out.println(options);
//    }

}