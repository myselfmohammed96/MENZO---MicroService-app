package com.menzo.User_Service.Staff.Tasks.Service;

import com.menzo.User_Service.Staff.Tasks.Entity.Module;
import com.menzo.User_Service.Staff.Tasks.Repository.ModuleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleQueryService {

    @Autowired
    private ModuleRepository moduleRepo;


    /*
    *
    *   Get module by module code
    *
    */
    public Module getModuleByCode(String moduleCode) {
        return moduleRepo.findByModuleCode(moduleCode)
                .orElseThrow(() -> new EntityNotFoundException("Module not found with module code: " + moduleCode));
    }
}
