package com.menzo.Product_Service.Variation.Service;

import com.menzo.Product_Service.Exception.DuplicateVariationOptionException;
import com.menzo.Product_Service.GlobalComponents.Service.UtilityService;
import com.menzo.Product_Service.Variation.Dto.CreateOptionDto;
import com.menzo.Product_Service.Variation.Dto.OptionDto;
import com.menzo.Product_Service.Variation.Entity.ColorCode;
import com.menzo.Product_Service.Variation.Entity.Variation;
import com.menzo.Product_Service.Variation.Entity.VariationOption;
import com.menzo.Product_Service.Variation.Repository.VariationsRepository;
import com.menzo.Product_Service.Variation.Repository.VariationOptionsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OptionCommandService {

    private static final Logger logger = LoggerFactory.getLogger(OptionCommandService.class);

    @Autowired
    private VariationsRepository variationsRepo;

    @Autowired
    private VariationOptionsRepository optionsRepo;

    @Autowired
    private UtilityService utilityService;


    /*
    *
    *   Add new variation option
    *
    */
    @Transactional
    public VariationOption addNewOption(CreateOptionDto newOption) {

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
            if (newOption.getHexCode() == null || newOption.getHexCode().isEmpty()) {
                throw new IllegalArgumentException("Hex code for color option required.");
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
                    .colorHexCode(newOption.getHexCode())
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


    /*
    *
    *   Update variation option
    *   Option identified by ID
    *
    */
    @Transactional
    public VariationOption updateOption(Long optionId, OptionDto latestOption) {

        //  fetching variation option by ID
        VariationOption option = optionsRepo.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Variation option not found with ID: " + optionId));

        //  updating variation option
        if (option.getVariation().getVariationName().equals("Colors")) {
            ColorCode colorCode = option.getColorCode();
            colorCode.setColorHexCode(
                    latestOption.getColorCodeHex() != null
                            && !latestOption.getColorCodeHex().isEmpty()
                            ? latestOption.getColorCodeHex()
                            : colorCode.getColorHexCode()
            );
            colorCode.setColorAbbreviation(
                    latestOption.getOptionValue() != null
                            && !latestOption.getOptionValue().isEmpty()
                            ? utilityService.generateAbbreviation("Colors", latestOption.getOptionValue())
                            : colorCode.getColorAbbreviation()
            );
            option.setColorCode(colorCode);
        }
        option.setOptionValue(
                latestOption.getOptionValue() != null
                        && !latestOption.getOptionValue().isEmpty()
                        ? latestOption.getOptionValue()
                        : option.getOptionValue()
        );
        logger.info("Updating variation option with ID: {}", optionId);
        return optionsRepo.save(option);
    }


    /*
    *
    *   Update variation option active status
    *   Option identified by option ID
    *
    */
    public boolean updateOptionActiveStatus(Long optionId, boolean isActive) {

        //  fetching variation by ID
        VariationOption option = optionsRepo.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Variation option not found with ID: " + optionId));

        //  updating variation active status
        option.setActive(isActive);
        return optionsRepo.save(option).isActive();
    }


    /*
    *
    *   Delete variation option
    *   Option identified by option ID
    *
    */
    public boolean deleteOption(Long optionId) {

        //  fetching variation option by ID
        VariationOption option = optionsRepo.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Variation option not found with ID: " + optionId));

        //  delete check validation
        if (option.isDeleted())
            throw new RuntimeException("Variation option with ID (" + optionId + ") already deleted.");

        //  soft delete: set isDelete to true if not already
        logger.info("Deleting variation option with ID {}", optionId);
        option.setDeleted(true);
        option.setDeletedAt(LocalDateTime.now());
        VariationOption softDeletedOption = optionsRepo.save(option);
        return softDeletedOption.isDeleted();
    }
}
