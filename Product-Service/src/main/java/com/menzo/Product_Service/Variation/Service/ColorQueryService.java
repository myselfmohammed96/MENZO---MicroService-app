package com.menzo.Product_Service.Variation.Service;

import com.menzo.Product_Service.Variation.Entity.ColorCode;
import com.menzo.Product_Service.Variation.Repository.ColorCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorQueryService {

    private static final Logger logger = LoggerFactory.getLogger(ColorQueryService.class);

    @Autowired
    private ColorCodeRepository colorRepo;


    /*
     *
     *   Get color by color code ID
     *
     */
    public ColorCode getColorCodeEntityById(Long colorId) {
        return colorRepo.findById(colorId)
                .orElseThrow(() -> new EntityNotFoundException("Color code not found with ID: " + colorId));
    }

}
