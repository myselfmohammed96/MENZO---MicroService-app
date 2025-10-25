package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Dto.VariationsDto.*;
import com.menzo.Product_Service.Entity.ColorCode;
import com.menzo.Product_Service.Entity.Variation;
import com.menzo.Product_Service.Entity.VariationOption;
import com.menzo.Product_Service.Exception.DuplicateVariationException;
import com.menzo.Product_Service.Exception.DuplicateVariationOptionException;
import com.menzo.Product_Service.Repository.VariationsOptionsRepo;
import com.menzo.Product_Service.Repository.VariationsRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VariationsService {

    private static final Logger log = LoggerFactory.getLogger(VariationsService.class);

    @Autowired
    private VariationsRepo variationsRepo;

    @Autowired
    private VariationsOptionsRepo optionsRepo;

    @Autowired
    private UtilityService utilityService;

//    ********* Variation *********

    //  Add new variation
//    public Variation addNewVariation(CreateVariationDto newVariation) {
//        if (variationsRepo.existsByVariationName(newVariation.getVariationName())) {
//            log.error("Variation '{}' already exists", newVariation.getVariationName());
//            throw new DuplicateVariationException("Variation already exists.");
//        }
//        Variation newSavedVariation = new Variation(newVariation.getVariationName());
//        log.info("Saving new variation: {}", newVariation.getVariationName());
//        return variationsRepo.save(newSavedVariation);
//    }

    //  Update variation by id
//    public Variation updateVariation(Long variationId, VariationDto latestVariation) {
//        Variation variation = variationsRepo.findVariationById(variationId)
//                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + variationId));
//        variation.setVariationName(latestVariation.getVariationName() != null && !latestVariation.getVariationName().isEmpty() ? latestVariation.getVariationName() : variation.getVariationName());
//        log.info("Updated variation with ID: {}", variationId);
//        return variationsRepo.save(variation);
//    }

    //  Delete variation by id
//    @Transactional
//    public boolean deleteVariation(Long variationId) {
//        boolean variationExists = variationsRepo.existsById(variationId);
//        if (!variationExists) {
//            log.error("Variation not found with ID {}", variationId);
//            throw new EntityNotFoundException("Variation not found with ID: " + variationId);
//        }
//        log.info("Deleting variation with ID {}", variationId);
//        variationsRepo.deleteVariationById(variationId);
//        boolean exists = variationsRepo.existsById(variationId);
//        if (!exists) {
//            log.info("Variation with ID {} successfully deleted", variationId);
//            return true;
//        } else {
//            log.error("Variation with ID {} could not be deleted", variationId);
//            return false;
//        }
//    }



//    ********* Variation options *********

    //  Add new variation option
//    public VariationOption addNewOption(CreateVariationOptionDto newOption) {
//
//        // input validation
//        if (optionsRepo.existsByOptionValueAndVariationId(
//                newOption.getOptionValue(),
//                newOption.getVariationId()
//        )) {
//            log.error("Option '{}' already exists under Variation ID {}", newOption.getOptionValue(), newOption.getVariationId());
//            throw new DuplicateVariationOptionException("Variation option already exists under this variation.");
//        }
//
//        // fetching variation object by ID
//        Variation variation = variationsRepo.findVariationById(newOption.getVariationId())
//                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + newOption.getVariationId()));
//
//        // saving Variation Option - with respect to 'COLOR' or 'NON-COLOR' variation
//        VariationOption newVariationOption;
//        if (variation.getVariationName().equals("Colors")) {
//            if (newOption.getColorCode() == null || newOption.getColorCode().isEmpty()) {
//                throw new IllegalArgumentException("Color code required.");
//            }
//            VariationOption option = VariationOption.builder()
//                    .optionValue(newOption.getOptionValue())
//                    .colorCode(null)
//                    .variation(variation)
//                    .build();
//            newVariationOption = optionsRepo.save(option);
//
//            String colorAbbreviation = utilityService.generateAbbreviation(
//                    "Colors",
//                    newOption.getOptionValue()
//            );
//            ColorCode colorCode = ColorCode.builder()
//                    .colorOption(newVariationOption)
//                    .colorCode(newOption.getColorCode())
//                    .colorAbbreviation(colorAbbreviation)
//                    .build();
//            newVariationOption.setColorCode(colorCode);
//        } else {
//            newVariationOption = VariationOption.builder()
//                    .optionValue(newOption.getOptionValue())
//                    .variation(variation)
//                    .build();
//        }
//        log.info("Saving new variation option under variation {}: {}", variation.getVariationName(), newOption.getOptionValue());
//        return optionsRepo.save(newVariationOption);
//    }

//    public VariationOption addNewOption(CreateVariationOptionDto newOption) {
//        if (optionsRepo.existsByOptionValueAndVariationId(newOption.getOptionValue(), newOption.getVariationId())) {
//            log.error("Option '{}' already exists under Variation ID {}", newOption.getOptionValue(), newOption.getVariationId());
//            throw new DuplicateVariationOptionException("Variation option already exists under this variation.");
//        }
//        Variation variation = variationsRepo.findVariationById(newOption.getVariationId())
//                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + newOption.getVariationId()));
//
//        if(variation.getVariationName().equals("Colors")) {
//            VariationOption newVariationOption = new VariationOption(
//                    newOption.getOptionValue(),
//                    variation
//            );
//            VariationOption savedOption = optionsRepo.save(newVariationOption);
////            savedOption.setColorCode(new ColorCode(
////                    savedOption,
////                    "#fff",
////                    "ABC")
////            );
//            return optionsRepo.save(savedOption);
//        } else {
////            VariationOption newVariationOption = new VariationOption(
////                    newOption.getOptionValue(),
////                    variation
////            );
//            log.info("Saving new variation option under variation {}: {}", variation.getVariationName(), newOption.getOptionValue());

    /// /            return optionsRepo.save(newVariationOption);
//        }
//    }

    //  Update variation option by id
    public VariationOption updateOption(Long optionId, OptionDto latestVariationOption) {
        VariationOption option = optionsRepo.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Variation option not found with ID: " + optionId));
        option.setOptionValue(latestVariationOption.getOptionValue() != null && !latestVariationOption.getOptionValue().isEmpty() ? latestVariationOption.getOptionValue() : option.getOptionValue());
        log.info("Updated variation option with ID: {}", optionId);
        VariationOption updatedOption = optionsRepo.save(option);

        return updatedOption;
    }

    //  Delete variation option by id
    public boolean deleteOption(Long optionId) {
        boolean optionExists = optionsRepo.existsById(optionId);
        if (!optionExists) {
            log.error("Variation option not found with ID {}", optionId);
            throw new EntityNotFoundException("Variation option not found with ID: " + optionId);
        }
        log.info("Deleting variation option with ID {}", optionId);
        optionsRepo.deleteById(optionId);
        boolean exists = optionsRepo.existsById(optionId);
        if (!exists) {
            log.info("Variation option with ID {} successfully deleted", optionId);
            return true;
        } else {
            log.error("Variation option with ID {} could not be deleted", optionId);
            return false;
        }
    }

}
