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

    private static final Logger logger = LoggerFactory.getLogger(VariationsService.class);

    @Autowired
    private VariationsRepo variationsRepo;

    @Autowired
    private VariationsOptionsRepo optionsRepo;

    @Autowired
    private UtilityService utilityService;

//    ********* Variation *********

    //  Add new variation - TESTED
    public Variation addNewVariation(CreateVariationDto newVariation) {

        //  duplicate - existence validation
        if (variationsRepo.existsByVariationName(newVariation.getVariationName())) {
            logger.error("Variation '{}' already exists", newVariation.getVariationName());
            throw new DuplicateVariationException("Variation already exists.");
        }

        //  saving new Variation
        Variation variation = Variation.builder()
                .variationName(newVariation.getVariationName())
                .isDeleted(false)
                .build();
        logger.info("Saving new variation: {}", newVariation.getVariationName());
        return variationsRepo.save(variation);
    }

    //  Update variation by id - TESTED
    public Variation updateVariation(Long variationId, VariationDto latestVariation) {

        //  fetching variation by ID
        Variation variation = variationsRepo.findById(variationId)
                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + variationId));

        //  updating variation
        variation.setVariationName(
                latestVariation.getVariationName() != null
                        && !latestVariation.getVariationName().isEmpty()
                        ? latestVariation.getVariationName()
                        : variation.getVariationName()
        );
        logger.info("Updating variation with ID: {}", variationId);
        return variationsRepo.save(variation);
    }

    //  Delete variation by id - TESTED
    //  ## on delete cascade required for variation - variation option
    public boolean deleteVariation(Long variationId) {

        //  fetching variation by ID
        Variation variation = variationsRepo.findById(variationId)
                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + variationId));

        //  delete check validation
        if (variation.getIsDeleted()) throw new RuntimeException("Variation with ID (" + variationId + ") already deleted.");

        //  soft delete: set isDelete to true if not already
        logger.info("Deleting variation with ID {}", variationId);
        variation.setIsDeleted(true);
        Variation softDeletedVariation = variationsRepo.save(variation);
        return softDeletedVariation.getIsDeleted();
    }



//    ********* Variation options *********

    //  Add new variation option
    @Transactional
    public VariationOption addNewOption(CreateVariationOptionDto newOption) {

        // duplicate existence validation
        if (optionsRepo.existsByOptionValueAndVariationId(
                newOption.getOptionValue(),
                newOption.getVariationId()
        )) {
            logger.error("Option '{}' already exists under Variation ID {}", newOption.getOptionValue(), newOption.getVariationId());
            throw new DuplicateVariationOptionException("Variation option already exists under this variation.");
        }

        // fetching variation object by ID
        Variation variation = variationsRepo.findById(newOption.getVariationId())
                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + newOption.getVariationId()));

        // saving Variation Option - with respect to 'COLOR' or 'NON-COLOR' variation
        VariationOption newVariationOption;
        if (variation.getVariationName().equals("Colors")) {
            if (newOption.getColorCode() == null || newOption.getColorCode().isEmpty()) {
                throw new IllegalArgumentException("Color code required.");
            }
            VariationOption option = VariationOption.builder()
                    .optionValue(newOption.getOptionValue())
                    .colorCode(null)
                    .variation(variation)
                    .build();
            newVariationOption = optionsRepo.save(option);

            //  generating abbreviation
            String colorAbbreviation = utilityService.generateAbbreviation(
                    "Colors",
                    newOption.getOptionValue()
            );
            ColorCode colorCode = ColorCode.builder()
                    .colorOption(newVariationOption)
                    .colorCode(newOption.getColorCode())
                    .colorAbbreviation(colorAbbreviation)
                    .build();
            newVariationOption.setColorCode(colorCode);
        } else {
            newVariationOption = VariationOption.builder()
                    .optionValue(newOption.getOptionValue())
                    .variation(variation)
                    .build();
        }
        logger.info("Saving new variation option under variation {}: {}", variation.getVariationName(), newOption.getOptionValue());
        return optionsRepo.save(newVariationOption);
    }

    //  Update variation option by id
    public VariationOption updateOption(Long optionId, OptionDto latestVariationOption) {
        VariationOption option = optionsRepo.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Variation option not found with ID: " + optionId));
        option.setOptionValue(latestVariationOption.getOptionValue() != null && !latestVariationOption.getOptionValue().isEmpty() ? latestVariationOption.getOptionValue() : option.getOptionValue());
        logger.info("Updated variation option with ID: {}", optionId);
        VariationOption updatedOption = optionsRepo.save(option);

        return updatedOption;
    }

    //  Delete variation option by id
    public boolean deleteOption(Long optionId) {
        boolean optionExists = optionsRepo.existsById(optionId);
        if (!optionExists) {
            logger.error("Variation option not found with ID {}", optionId);
            throw new EntityNotFoundException("Variation option not found with ID: " + optionId);
        }
        logger.info("Deleting variation option with ID {}", optionId);
        optionsRepo.deleteById(optionId);
        boolean exists = optionsRepo.existsById(optionId);
        if (!exists) {
            logger.info("Variation option with ID {} successfully deleted", optionId);
            return true;
        } else {
            logger.error("Variation option with ID {} could not be deleted", optionId);
            return false;
        }
    }

}
