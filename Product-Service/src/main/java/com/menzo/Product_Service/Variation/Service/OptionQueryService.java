package com.menzo.Product_Service.Variation.Service;

import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableOptionFilter;
import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableVariationFilter;
import com.menzo.Product_Service.Variation.Dto.OptionDto;
import com.menzo.Product_Service.Variation.Dto.VariationOptionsDto;
import com.menzo.Product_Service.Variation.Entity.Variation;
import com.menzo.Product_Service.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Variation.Repository.VariationOptionsRepository;
import com.menzo.Product_Service.Variation.Repository.VariationsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OptionQueryService {

    private static final Logger logger = LoggerFactory.getLogger(OptionQueryService.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VariationsRepository variationsRepo;

    @Autowired
    private VariationOptionsRepository optionsRepo;


    /*
     *
     *   Get options by variation name
     *
     */
//    public List<String> getOptionsByVariationName(Long categoryId, String variationName) {
//        if (categoryId == null) {
//            List<OptionMinimalDto> optionsDtoList = variationsRepo.findOptionsByVariationName(variationName);
//            return optionsDtoList.stream()
//                    .map(dto -> dto.getOptionValue())
//                    .collect(Collectors.toList());
//        } else {
//            return null;
//        }
//    }


    /*
     *
     *   Get options by list of IDs
     *
     */
//    public List<VariationOption> getOptionsByIds(List<Long> idList) {
//        return optionsRepo.findByIdIn(idList);
//    }


    /*
     *
     *   Get option by ID
     *
     */
//    public VariationOption getOptionById(Long id) {
//        return optionsRepo.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("No variation option found for Option ID: " + id));
//    }


    /*
     *
     *   Get variation option by ID - with validating the variation name
     *   used to get color option by ID - with validating that it is indeed a color option
     *
     */
//    public VariationOption getOptionByIdAndVariationName(Long optionId, String variationName) {
//        Variation variation = variationsRepo.findByVariationName(variationName)
//                .orElseThrow(() -> new EntityNotFoundException("Variation not found with name: " + variationName));
//
//        logger.info("Returning '{}' variation option for option ID: {}", variationName, optionId);
//        return variation.getOptions().stream()
//                .filter(opt -> opt.getId() == optionId)
//                .findFirst()
//                .orElseThrow(() -> new EntityNotFoundException("'" + variationName + "' option not found with ID: " + optionId));

    /// /        return OptionDto.builder()
    /// /                .optionId(option.getId())
    /// /                .optionValue(option.getOptionValue())
    /// /                .variationId(variation.getId())
    /// /                .variationName(variation.getVariationName())
    /// /                .colorCode(option.getColorCode().getColorCode())
    /// /                .build();
//    }


    /*
     *
     *  Get list of variation options by variation ID
     *
     */
    @Transactional
    @EnableVariationFilter
    @EnableOptionFilter
    public VariationOptionsDto getOptionsByVariationId(Long variationId) {

        //  fetching variation
        Variation variation = variationsRepo.findById(variationId)
                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + variationId));

        //  enabling filter to exclude soft deleted options
//        Session session = entityManager.unwrap(Session.class);
//        session.enableFilter("optionActiveFilter")
//                .setParameter("isDeleted", false);

        //  fetching options
        List<VariationOption> options = optionsRepo.findByVariationId(variationId);
        if (variation.getVariationName().equals("Colors")) {
            Set<OptionDto> optionDtos = options.stream().map(opt ->
                    OptionDto.builder()
                            .optionId(opt.getOptionId())
                            .optionValue(opt.getOptionValue())
                            .colorCodeHex(opt.getColorCode().getColorHexCode())
                            .build()
            ).collect(Collectors.toSet());

            return VariationOptionsDto.builder()
                    .variationName(variation.getVariationName())
                    .options(optionDtos)
                    .build();

        } else {
            Set<OptionDto> optionDtos = options.stream().map(opt ->
                            OptionDto.builder()
                                    .optionId(opt.getOptionId())
                                    .optionValue(opt.getOptionValue())
                                    .build()
            ).collect(Collectors.toSet());

            return VariationOptionsDto.builder()
                    .variationName(variation.getVariationName())
                    .options(optionDtos)
                    .build();
        }
    }


    /*
     *
     *   Get list of option IDs by variation name
     *
     */
//    @Transactional
//    public List<Long> getOptionIdsByVariation(String variationName) {
//        return getVariationWithOptionsByVariationName(variationName).getOptions()
//                .stream()
//                .map(opt -> opt.getOptionId())
//                .collect(Collectors.toList());
//    }
}
