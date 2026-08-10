package com.menzo.User_Service.Staff.Tasks.Service;

import com.menzo.User_Service.Staff.Tasks.Entity.Module;
import com.menzo.User_Service.Staff.Tasks.Repository.ModuleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ModuleCommandService {

    @Autowired
    private ModuleRepository moduleRepo;


    /*
    *
    *   Update module active status
    *   Module identified by module ID
    *
    */
    public boolean updateModuleActiveStatus(UUID moduleId, boolean isActive) {

        //  fetching module by ID
        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new EntityNotFoundException("Module not found with ID: " + moduleId));

        //  updating module active status
        module.setActive(isActive);
        return moduleRepo.save(module).isActive();
    }
}
