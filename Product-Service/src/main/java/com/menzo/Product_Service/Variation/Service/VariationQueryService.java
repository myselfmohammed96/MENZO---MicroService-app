package com.menzo.Product_Service.Variation.Service;

import com.menzo.Product_Service.Enum.Components;
import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableOptionFilter;
import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableVariationFilter;
import com.menzo.Product_Service.Variation.Dto.*;
import com.menzo.Product_Service.Variation.Entity.Variation;
import com.menzo.Product_Service.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Variation.Repo.VariationOptionsRepository;
import com.menzo.Product_Service.Variation.Repo.VariationsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VariationQueryService {

    private static final Logger logger = LoggerFactory.getLogger(VariationQueryService.class);

    @Autowired
    private VariationsRepository variationsRepo;

    @Autowired
    private VariationOptionsRepository optionsRepo;

    @PersistenceContext
    private EntityManager entityManager;


    /*
     *
     *   Get all variations
     *   With variation options
     *
     */
    @Transactional
    @EnableVariationFilter
    @EnableOptionFilter
    public List<VariationWithOptionsDto> getAllVariationsWithOptions() {

        //  fetch all variations
        List<Variation> variations = variationsRepo.findAll();

        //  enabling filter to exclude soft deleted options
//        Session session = entityManager.unwrap(Session.class);
//        session.enableFilter("optionActiveFilter")
//                .setParameter("isDeleted", false);

        //  build variations list with options
        List<VariationWithOptionsDto> variationsList = new ArrayList<>();
        for (Variation v : variations) {
            Set<OptionMinimalDto> options = new HashSet<>();
            for (VariationOption option : v.getOptions()) {
                OptionMinimalDto optionDto = OptionMinimalDto.builder()
                        .optionId(option.getOptionId())
                        .optionValue(option.getOptionValue())
                        .build();
                options.add(optionDto);
            }
            VariationWithOptionsDto variationDto = VariationWithOptionsDto.builder()
                    .id(v.getVariationId())
                    .variationName(v.getVariationName())
                    .options(options)
                    .build();
            variationsList.add(variationDto);
        }
        return variationsList;
    }


    /*
     *
     *   Get all variations
     *   Without variation options
     *
     */
    @Transactional
    @EnableVariationFilter
    public List<VariationDto> getAllVariationsWithoutOptions() {
//        Session session = entityManager.unwrap(Session.class);
//        session.enableFilter("variationActiveFilter")
//                .setParameter("isDeleted", false);

        //  fetch variations
        List<Variation> variations = variationsRepo.findAll();

        return variations.stream()
                .map(v -> new VariationDto(
                        v.getVariationId(),
                        v.getVariationName(),
                        v.getCreatedAt())
                ).collect(Collectors.toList());
    }


    /*
     *
     *   Get all variations and options
     *   Associated with sub-category
     *   Identified by sub-category ID
     *
     *
     */
    @Transactional
    public List<VariationWithOptionsDto> getAllVariationsWithOptionsBySub(Components componentType,
                                                                          Long componentId) {
        Map<Long, VariationWithOptionsDto> variationMap = new HashMap<>();

        //  fetching data with sub-category ID
        List<Object[]> rows;
        if (componentType.equals(Components.CATEGORY)) {
            rows = variationsRepo.findAllByCategoryId(componentId, true, false);
        } else if (componentType.equals(Components.SUB_CATEGORY)) {
            rows = variationsRepo.findAllByCategoryId(componentId, false, false);
        } else {
            throw new IllegalArgumentException("Invalid componentType. must be 'CATEGORY' or 'SUB-CATEGORY': " + componentType);
        }

        //  organizing data in dto
        rows.stream()
                .filter(row -> {
                    String variationName = (String) row[1];
                    return (!variationName.equals("Colors") && !variationName.equals("Size"));
                })
                .forEach(row -> {
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
                    OptionMinimalDto option = OptionMinimalDto.builder()
                            .optionId(optionId)
                            .optionValue(optionValue)
                            .build();
                    Set<OptionMinimalDto> opt = variation.getOptions() != null
                            ? variation.getOptions()
                            : new HashSet<OptionMinimalDto>();
                    opt.add(option);
                    variation.setOptions(opt);
                });
        return new ArrayList<>(variationMap.values());
    }


    /*
     *
     *   Get sizes or colors
     *   ## better to make this as a supplier.. fetching variation for "Size" explicitly
     *   ## filter for isDeleted false content
     *
     */
    @Transactional
    @EnableVariationFilter
    @EnableOptionFilter
    public VariationOptionsMinimalDto getVariationWithOptionsByVariationName(String variationName) {

        //  fetching variation by variation name
        Variation variation = variationsRepo.findByVariationName(variationName)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found for variation: " + variationName));

        //  building nested object for variation & the options
        VariationOptionsMinimalDto variationWithSizeList = VariationOptionsMinimalDto.builder()
                .variationId(variation.getVariationId())
                .variationName(variation.getVariationName())
                .build();

        //  extracting options
        if (variation.getVariationName().equals("Colors")) {
            List<OptionMinimalDto> options = variation.getOptions().stream()
                    .map(opt -> OptionMinimalDto.builder()
                            .optionId(opt.getOptionId())
                            .optionValue(opt.getOptionValue())
                            .colorCodeHex(opt.getColorCode().getColorHexCode())
                            .build()
                    ).collect(Collectors.toList());
            variationWithSizeList.setOptions(options);
        } else {
            List<OptionMinimalDto> options = variation.getOptions().stream()
                    .map(opt -> OptionMinimalDto.builder()
                            .optionId(opt.getOptionId())
                            .optionValue(opt.getOptionValue())
                            .build()
                    ).collect(Collectors.toList());
            variationWithSizeList.setOptions(options);
        }
        return variationWithSizeList;
    }

}
