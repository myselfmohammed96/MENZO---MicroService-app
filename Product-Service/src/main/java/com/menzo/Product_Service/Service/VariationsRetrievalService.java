package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.VariationsDto.OptionWithIdDto;
import com.menzo.Product_Service.Dto.VariationsDto.VariationDto;
import com.menzo.Product_Service.Dto.VariationsDto.VariationWithOptionsDto;
import com.menzo.Product_Service.Entity.Variation;
import com.menzo.Product_Service.Entity.VariationOption;
import com.menzo.Product_Service.Repository.VariationsOptionsRepo;
import com.menzo.Product_Service.Repository.VariationsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VariationsRetrievalService {

    private static final Logger log = LoggerFactory.getLogger(VariationsRetrievalService.class);

    @Autowired
    private VariationsRepo variationsRepo;

    @Autowired
    private VariationsOptionsRepo optionsRepo;

    // Variation

    public List<VariationWithOptionsDto> getAllVariationsWithOptions() {
        List<Variation> allVariations = variationsRepo.findAll();
        List<VariationWithOptionsDto> variationsList = new ArrayList<>();

        for (Variation variation: allVariations) {
            Set<OptionWithIdDto> options = new HashSet<>();
            for (VariationOption option: variation.getOptions()) {
                OptionWithIdDto optionDto = new OptionWithIdDto(option);
                options.add(optionDto);
            }
            VariationWithOptionsDto variationDto = new VariationWithOptionsDto(variation.getId(), variation.getVariationName(), options);
            variationsList.add(variationDto);
        }
        return variationsList;
    }

    public List<?> getAllVariationsWithOptionsBySub(Long subCategoryId) {
        List<Object[]> rows = variationsRepo.findAllByCategoryId(subCategoryId);
        Map<Long, VariationWithOptionsDto> variationMap = new HashMap<>();

        for(Object[] row: rows) {
            Long variationId = ((Number) row[0]).longValue();
            String variationName = (String) row[1];
            Long optionId = ((Number) row[2]).longValue();
            String optionValue = (String) row[3];

            VariationWithOptionsDto variation = variationMap.computeIfAbsent(variationId, id -> {
                VariationWithOptionsDto v =  new VariationWithOptionsDto();
                v.setId(id);
                v.setVariationName(variationName);
                return v;
            });

            OptionWithIdDto option = new OptionWithIdDto();
            option.setId(optionId);
            option.setOptionValue(optionValue);
            variation.getOptions().add(option);
        }
        return new ArrayList<>(variationMap.values());
    }

    public List<VariationDto> getAllVariations() {
        List<VariationDto> allVariations = variationsRepo.findAllVariationIdAndNames();
        return allVariations;
    }

    public List<String> getOptionsByVariationName(Long categoryId, String variationName) {
        if (categoryId == null) {
            List<OptionWithIdDto> optionsDtoList = variationsRepo.findOptionsByVariationName(variationName);
            return optionsDtoList.stream().map(dto -> dto.getOptionValue()).collect(Collectors.toList());
        } else {
            return null;
        }
    }

}
