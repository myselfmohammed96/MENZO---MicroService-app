package com.menzo.Communication_Service.Otp.Repository;

import com.menzo.Communication_Service.Otp.Entity.OtpCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface OtpCacheRepository extends JpaRepository<OtpCache, UUID> {

    @Modifying
    @Query("DELETE FROM OtpCache c WHERE c.expiresAt < :now")
    int deleteExpired(@Param("now") LocalDateTime now);

}
