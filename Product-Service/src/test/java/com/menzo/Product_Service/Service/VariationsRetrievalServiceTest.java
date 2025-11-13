package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.VariationsDto.NestedVariationDto;
import com.menzo.Product_Service.Dto.VariationsDto.OptionDto;
import com.menzo.Product_Service.Dto.VariationsDto.VariationDto;
import com.menzo.Product_Service.Dto.VariationsDto.VariationWithOptionsDto;
import com.menzo.Product_Service.Entity.VariationOption;
import com.menzo.Product_Service.Enum.Components;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class VariationsRetrievalServiceTest {

    @Autowired
    private VariationsRetrievalService variationsRetrievalService;

    @Test
    public void testGetAllVariationsWithOptions() {
        List<VariationWithOptionsDto> variations = variationsRetrievalService.getAllVariationsWithOptions();
        System.out.println(variations);
    }

    @Test
    public void testGetAllVariationsWithOptionsBySub() {
        List<VariationWithOptionsDto> data = variationsRetrievalService.getAllVariationsWithOptionsByCategory(
                Components.CATEGORY,
                2L
        );
        data.stream().forEach(d -> System.out.println(d));
        System.out.println(data.size());
    }

    @Test
    public void testGetAllVariations() {
        List<VariationDto> variationsList = variationsRetrievalService.getAllVariations();
        System.out.println(variationsList);
    }

    @Test
    public void testGetOptionsByVariationName() {
        List<String> options = variationsRetrievalService.getOptionsByVariationName(
                null,
                "Size"
        );
        System.out.println(options);
    }

    @Test
    public void testGetSizes() {
        NestedVariationDto sizes = variationsRetrievalService.getSizes("Size");
        System.out.println(sizes);
    }

    @Test
    @Transactional
    public void testGetOptionsByIds() {
        List<VariationOption> options = variationsRetrievalService.getOptionsByIds(Arrays.asList(
                3L,
                4L,
                5L,
                7L,
                8L
        ));
        System.out.println(options);
    }

    @Test
    public void testGetOptionById() {
        VariationOption option = variationsRetrievalService.getOptionById(3L);
        System.out.println(option);
    }

    @Test
    public void testGetOptionsByVariationId() {
        List<OptionDto> options = variationsRetrievalService
                .getOptionsByVariationId(9L);
        System.out.println(options);
        System.out.println(options.size());
    }

    @Test
    public void testGetColorOptionsByVariationId() {
        List<OptionDto> options = variationsRetrievalService
                .getOptionsByVariationId(3L);
        System.out.println(options);
        System.out.println(options.size());
    }

    @Test
    public void testGetOptionIdsByVariation() {
        List<Long> options = variationsRetrievalService.getOptionIdsByVariation("Colors");
        System.out.println(options);
    }

}