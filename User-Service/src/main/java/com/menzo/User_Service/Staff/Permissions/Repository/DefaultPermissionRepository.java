package com.menzo.User_Service.Staff.Permissions.Repository;

import com.menzo.User_Service.Staff.Permissions.Entity.DefaultPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DefaultPermissionRepository extends JpaRepository<DefaultPermission, UUID> {
}
