package com.menzo.Identity_Service.Auth.Repository;

import com.menzo.Identity_Service.Auth.Entity.UserTokenCutoff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserTokenCutoffRepository extends JpaRepository<UserTokenCutoff, UUID> {
}
