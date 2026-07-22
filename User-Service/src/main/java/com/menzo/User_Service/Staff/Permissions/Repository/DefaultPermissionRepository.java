package com.menzo.User_Service.Staff.Permissions.Repository;

import com.menzo.User_Service.Staff.Permissions.Entity.DefaultPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultPermissionRepository extends JpaRepository<DefaultPermission, Long> {
}
