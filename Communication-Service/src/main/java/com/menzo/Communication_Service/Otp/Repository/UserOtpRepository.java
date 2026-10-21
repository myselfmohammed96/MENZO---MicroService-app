package com.menzo.Communication_Service.Otp.Repository;

import com.menzo.Communication_Service.Global.Enum.Purpose;
import com.menzo.Communication_Service.Otp.Entity.UserOtp;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserOtpRepository extends JpaRepository<UserOtp, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserOtp u WHERE u.userEmail = :email AND u.purpose = :purpose")
    Optional<UserOtp> findForUpdate(@Param("email") String email, @Param("purpose") Purpose purpose);

}
