package com.menzo.User_Service.Staff.Departments.Repository;

import com.menzo.User_Service.Staff.Departments.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
