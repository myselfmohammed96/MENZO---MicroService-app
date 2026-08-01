package com.menzo.Product_Service.Variation.Service;

import com.menzo.Product_Service.Exception.DuplicateVariationException;
import com.menzo.Product_Service.GlobalComponents.CustomAnnotations.Annotations.EnableVariationFilter;
import com.menzo.Product_Service.Variation.Entity.Variation;
import com.menzo.Product_Service.Variation.Repository.VariationsRepository;
import com.menzo.Product_Service.Variation.Dto.CreateVariationDto;
import com.menzo.Product_Service.Variation.Dto.VariationDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VariationCommandService {

    private static final Logger logger = LoggerFactory.getLogger(VariationCommandService.class);

    @Autowired
    private VariationsRepository variationRepo;


    /*
     *
     *   Add new variation
     *
     */
    @Transactional
    @EnableVariationFilter
    public Variation addNewVariation(CreateVariationDto newVariation) {

        //  duplicate - existence validation
        if (variationRepo.existsByVariationName(newVariation.getVariationName())) {
            logger.error("Variation '{}' already exists", newVariation.getVariationName());
            throw new DuplicateVariationException("Variation already exists.");
        }

        //  saving new Variation
        Variation variation = Variation.builder()
                .variationName(newVariation.getVariationName())
                .build();
        logger.info("Saving new variation: {}", newVariation.getVariationName());
        return variationRepo.save(variation);
    }


    /*
     *
     *  Update variation
     *  Variation identified by variation ID
     *
     */
    @Transactional
    @EnableVariationFilter
    public Variation updateVariation(Long variationId, VariationDto latestVariation) {

        //  fetching variation by ID
        Variation variation = variationRepo.findById(variationId)
                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + variationId));

        //  updating variation
        variation.setVariationName(
                latestVariation.getVariationName() != null
                        && !latestVariation.getVariationName().isEmpty()
                        ? latestVariation.getVariationName()
                        : variation.getVariationName()
        );
        logger.info("Updating variation with ID: {}", variationId);
        return variationRepo.save(variation);
    }


    /*
    *
    *   Update variation active status
    *   Variation identified by variation ID
    *
    */
    @Transactional
    @EnableVariationFilter
    public boolean updateVariationActiveStatus(Long variationId, boolean isActive) {

        //  fetching variation by ID
        Variation variation = variationRepo.findById(variationId)
                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + variationId));

        //  updating variation active status
        variation.setActive(isActive);
        return variationRepo.save(variation).isActive();
    }


    /*
    *
    *   Delete variation
    *   Variation identified by variation ID
    *   ## On delete - cascade required for variation - variation option
    *
    */
    @Transactional
    @EnableVariationFilter
    public boolean deleteVariation(Long variationId) {

        //  fetching variation by ID
        Variation variation = variationRepo.findById(variationId)
                .orElseThrow(() -> new EntityNotFoundException("Variation not found with ID: " + variationId));

        //  soft delete: set isDelete to true if not already
        logger.info("Deleting variation with ID {}", variationId);
        variation.setDeleted(true);
        variation.setDeletedAt(LocalDateTime.now());
        variationRepo.save(variation);
        return true;
    }

}
