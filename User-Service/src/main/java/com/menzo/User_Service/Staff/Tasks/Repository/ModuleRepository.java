package com.menzo.User_Service.Staff.Tasks.Repository;

import com.menzo.User_Service.Staff.Tasks.Entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {

    public Optional<Module> findByModuleCode(String moduleCode);

}
