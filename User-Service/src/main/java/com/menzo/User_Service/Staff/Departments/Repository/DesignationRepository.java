package com.menzo.User_Service.Staff.Departments.Repository;

import com.menzo.User_Service.Staff.Departments.Entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DesignationRepository extends JpaRepository<Designation, UUID> {

    public Optional<Designation> findByDesignationName(String designationName);

}
