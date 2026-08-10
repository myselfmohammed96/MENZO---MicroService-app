package com.menzo.User_Service.Staff.Departments.Repository;

import com.menzo.User_Service.Staff.Departments.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    public Optional<Department> findById(UUID departmentId);

    public boolean existsByDepartmentName(String departmentName);

    public boolean existsByDepartmentCode(String departmentCode);

}
