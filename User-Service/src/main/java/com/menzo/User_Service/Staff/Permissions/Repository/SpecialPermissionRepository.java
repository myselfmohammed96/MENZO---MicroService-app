package com.menzo.User_Service.Staff.Permissions.Repository;

import com.menzo.User_Service.Staff.Permissions.Entity.SpecialPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialPermissionRepository extends JpaRepository<SpecialPermission, Long> {
}
