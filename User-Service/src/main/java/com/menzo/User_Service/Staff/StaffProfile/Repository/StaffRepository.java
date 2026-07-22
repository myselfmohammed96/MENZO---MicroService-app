package com.menzo.User_Service.Staff.StaffProfile.Repository;

import com.menzo.User_Service.Staff.StaffProfile.Entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}
