package com.menzo.User_Service.DataSeeder.DesignationSeeder;

import com.menzo.User_Service.Staff.Departments.Entity.Designation;
import com.menzo.User_Service.Staff.Departments.Repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesignationSyncService {

    @Autowired
    private DesignationProperties designationProps;

    @Autowired
    private DesignationRepository designationRepo;


    /*
    *
    *   Sync designation details to designations table
    *
    */
    public void syncDesignations() {
        designationProps.getCore().forEach(designation -> {
            Designation newDesignation = designationRepo.findByDesignationName(designation)
                    .orElseGet(() -> Designation.builder()
                            .designationName(designation)
                            .build()
                    );
            designationRepo.save(newDesignation);
        });
    }
}
