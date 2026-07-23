package com.menzo.User_Service.Staff.Departments.Repository;

import com.menzo.User_Service.Staff.Departments.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    public Optional<Department> findById(Long departmentId);

    public boolean existsByDepartmentName(String departmentName);

    public boolean existsByDepartmentCode(String departmentCode);

}
