package com.menzo.Communication_Service.Otp.Repository;

import com.menzo.Communication_Service.Global.Enum.Purpose;
import com.menzo.Communication_Service.Otp.Entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserOtpRepository extends JpaRepository<UserOtp, UUID> {

    /*
     *
     *   check user OTP existence by user email
     *
     */
    boolean existsByUserEmail(String userEmail);


    /*
     *
     *   find user OTP by user email
     *
     */
    Optional<UserOtp> findByUserEmail(String userEmail);


    /*
     *
     *   find user OTP by user email & purpose
     *
     */
    Optional<UserOtp> findByUserEmailAndPurpose(String userEmail, Purpose purpose);

}
