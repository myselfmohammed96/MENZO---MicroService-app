package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.VariationsDto.*;
import com.menzo.Product_Service.Entity.Variation;
import com.menzo.Product_Service.Entity.VariationOption;
import com.menzo.Product_Service.Repository.VariationsOptionsRepo;
import com.menzo.Product_Service.Repository.VariationsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VariationsRetrievalService {

    private static final Logger logger = LoggerFactory.getLogger(VariationsRetrievalService.class);

    @Autowired
    private VariationsRepo variationsRepo;

    @Autowired
    private VariationsOptionsRepo optionsRepo;

    // Variation

    //  get all variations with their options - TESTED
    @Transactional
    public List<VariationWithOptionsDto> getAllVariationsWithOptions() {
        List<Variation> allVariations = variationsRepo.findAll();
        List<VariationWithOptionsDto> variationsList = new ArrayList<>();

        for (Variation variation : allVariations) {
            Set<OptionWithIdDto> options = new HashSet<>();
            for (VariationOption option : variation.getOptions()) {
                OptionWithIdDto optionDto = OptionWithIdDto.builder()
                        .id(option.getId())
                        .optionValue(option.getOptionValue())
                        .build();
                options.add(optionDto);
            }
            VariationWithOptionsDto variationDto = new VariationWithOptionsDto(
                    variation.getId(),
                    variation.getVariationName(),
                    options
            );
            variationsList.add(variationDto);
        }
        return variationsList;
    }


    //  get all variations associated with sub-category with options - by sub-category ID - TESTED
    public List<VariationWithOptionsDto> getAllVariationsWithOptionsBySub(Long subCategoryId) {

        //  fetching data with sub-category ID
        List<Object[]> rows = variationsRepo.findAllByCategoryId(subCategoryId);
        Map<Long, VariationWithOptionsDto> variationMap = new HashMap<>();

        //  organizing data in dto
        for (Object[] row : rows) {
            Long variationId = ((Number) row[0]).longValue();
            String variationName = (String) row[1];
            Long optionId = ((Number) row[2]).longValue();
            String optionValue = (String) row[3];

            VariationWithOptionsDto variation = variationMap.computeIfAbsent(variationId, id -> {
                VariationWithOptionsDto v = VariationWithOptionsDto.builder()
                        .id(id)
                        .variationName(variationName)
                        .build();
                return v;
            });
            OptionWithIdDto option = OptionWithIdDto.builder()
                    .id(optionId)
                    .optionValue(optionValue)
                    .build();
            Set<OptionWithIdDto> opt = variation.getOptions() != null
                    ? variation.getOptions()
                    : new HashSet<OptionWithIdDto>();
            opt.add(option);
            variation.setOptions(opt);
        }
        return new ArrayList<>(variationMap.values());
    }

    //  get all variations without options - TESTED
    public List<VariationDto> getAllVariations() {
        List<Variation> variations = variationsRepo.findAll();
        List<VariationDto> variationsList = variations.stream()
                .map(v -> new VariationDto(
                        v.getId(),
                        v.getVariationName(),
                        v.getCreatedAt())
                ).collect(Collectors.toList());
        return variationsList;
    }


    //  get variation-options by given variation name - TESTED
    public List<String> getOptionsByVariationName(Long categoryId, String variationName) {
        if (categoryId == null) {
            List<OptionWithIdDto> optionsDtoList = variationsRepo.findOptionsByVariationName(variationName);
            return optionsDtoList.stream()
                    .map(dto -> dto.getOptionValue())
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    //  get sizes - TESTED
    //  ## better to make this as a supplier.. fetching variation for "Size" explicitly
    //  ## filter for isDeleted false content
    @Transactional
    public NestedVariationDto getSizes(String variationName) {

        //  fetching variation by variation name
        Variation variation = variationsRepo.findByVariationName(variationName)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found for variation: " + variationName));

        //  building nested object for variation & the sizes
        NestedVariationDto variationWithSizeList = NestedVariationDto.builder()
                .id(variation.getId())
                .variationName(variation.getVariationName())
                .build();

        //  extracting sizes from variation options
        List<NestedVariationDto> sizes = variation.getOptions().stream()
                .map(opt -> {
                    return NestedVariationDto.builder()
                            .id(opt.getId())
                            .variationName(opt.getOptionValue())
                            .build();
                }).collect(Collectors.toList());
        variationWithSizeList.setOptions(sizes);
        return variationWithSizeList;
    }

    //  get options by list of IDs - TESTED
    public List<VariationOption> getOptionsByIds(List<Long> idList) {
        return optionsRepo.findByIdIn(idList);
    }

    //  get option by ID - TESTED
    public VariationOption getOptionById(Long id) {
        return optionsRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No variation option found for Option ID: " + id));
    }

    //  get options by variation ID - TESTED
    public List<OptionDto> getOptionsByVariationId(Long variationId) {
        Variation variation = variationsRepo.findById(variationId)
                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + variationId));
        List<VariationOption> options = optionsRepo.findByVariationId(variationId);
        if (variation.getVariationName().equals("Colors")) {
            return options.stream().map(opt -> {
                return OptionDto.builder()
                        .id(opt.getId())
                        .optionValue(opt.getOptionValue())
                        .colorCode(opt.getColorCode().getColorCode())
                        .build();
            }).collect(Collectors.toList());
        } else {
            return options.stream().map(opt -> {
                return OptionDto.builder()
                        .id(opt.getId())
                        .optionValue(opt.getOptionValue())
                        .build();
            }).collect(Collectors.toList());
        }
    }

    //  get list of option IDs by variation name - TESTED
    @Transactional
    public List<Long> getOptionIdsByVariation(String variationName) {
        return getSizes(variationName).getOptions()
                .stream()
                .map(opt -> opt.getId())
                .collect(Collectors.toList());
    }

}












