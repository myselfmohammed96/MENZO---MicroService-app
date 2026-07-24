package com.menzo.User_Service.Staff.Departments.Service;

import com.menzo.User_Service.Staff.Departments.Entity.Designation;
import com.menzo.User_Service.Staff.Departments.Repository.DesignationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesignationCommandService {

    @Autowired
    private DesignationRepository designationRepo;


    /*
    *
    *   Update designation active status
    *   Designation identified by designation ID
    *
    */
    public boolean updateDesignationActiveStatus(Integer designationId, boolean isActive) {

        //  fetching designation by ID
        Designation designation = designationRepo.findById(designationId)
                .orElseThrow(() -> new EntityNotFoundException("Designation not found with ID: " + designationId));

        //  updating designation active status
        designation.setActive(isActive);
        return designationRepo.save(designation).isActive();
    }
}
