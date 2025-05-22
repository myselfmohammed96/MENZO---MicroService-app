package com.menzo.Product_Service.Service;

import com.menzo.Product_Service.Entity.Variations;
import com.menzo.Product_Service.Repository.VariationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariationsService {

    @Autowired
    private VariationsRepo variationsRepo;

//    Variations

    public void addNewVariation(Variations newVariation) {
        variationsRepo.save(newVariation);
        System.out.println("Done: " + newVariation.getVariationName());
    }
}
