package com.menzo.Communication_Service.Otp.Repository;

import com.menzo.Communication_Service.Otp.Entity.OtpCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OtpCacheRepository extends JpaRepository<OtpCache, UUID> {
}
