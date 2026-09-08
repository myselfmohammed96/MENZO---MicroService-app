package com.menzo.Communication_Service.Otp.Service;

import com.menzo.Communication_Service.Email.Dto.HashDto;
import com.menzo.Communication_Service.Email.Service.EmailService;
import com.menzo.Communication_Service.Global.Feign.AuthFeign;
import com.menzo.Communication_Service.Global.Enum.Purpose;
import com.menzo.Communication_Service.Global.Feign.UserFeign;
import com.menzo.Communication_Service.Otp.Entity.OtpCache;
import com.menzo.Communication_Service.Otp.Entity.UserOtp;
import com.menzo.Communication_Service.Otp.Repository.OtpCacheRepository;
import com.menzo.Communication_Service.Otp.Repository.UserOtpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private AuthFeign authFeign;

    @Autowired
    private UserOtpRepository userOtpRepo;

    @Autowired
    private OtpCacheRepository otpCacheRepo;

    @Autowired
    private EmailService emailService;


    /*
     *
     *   Send new OTP
     *
     */
    @Transactional
    public boolean sendNewOtp(String userEmail, Purpose purpose) {

        //  check if user otp records & otp cache present in db..
        //  if present -> valid or not (active session is there or not)
        //  if valid/active -> use it.
        //  if not -> delete and create new (or) refresh

        //  *** New otp send ***
        //  create UserOtp record -> all refreshed
        //  not otp cache record
        //  generate & hash otp
        //  create otp cache record -> all refreshed
        //  calculate expiresAt and save


        //  check user OTP record
        boolean isNewUserOtp = !userOtpRepo.existsByUserEmail(userEmail);

        UserOtp userOtp = userOtpRepo.findByUserEmail(userEmail)
                .orElseGet(() -> {
                    UserOtp newUserOtp = UserOtp.builder()
                            .userEmail(userEmail)
                            .purpose(purpose)
                            .build();
                    return userOtpRepo.save(newUserOtp);
                });


        //  generate OTP
        String otp = generateOtp();

        //  hashing OTP


        // 1. Read user_otp record
        // 2. Check cooldown / block / count
        // 3. Update count + timestamp
        // 4. Create/update OTP
    }


    /*
     *
     *   Resend OTP
     *
     */
    @Transactional
    public boolean resendOtp(String userEmail, Purpose purpose) {
        //  get UserOtp by userEmail and purpose if exists
        UserOtp userOtp = userOtpRepo.findByUserEmailAndPurpose(userEmail, purpose)
                .orElseThrow(() -> new IllegalArgumentException("No active OTP verification process found."));

        //  ----- UserOtp validation -----
        LocalDateTime now = LocalDateTime.now();

        //  overall process lifetime check
        if (userOtp.getLastSentAt() != null && userOtp.getLastSentAt().plusHours(12).isBefore(now)) {
            throw new IllegalArgumentException("OTP verification process has expired. Please start again.");
        }

        //  block time check
        if (userOtp.getBlockedUntil() != null && userOtp.getBlockedUntil().isAfter(now)) {
            throw new RuntimeException("OTP resend is temporarily blocked. Please try again later.");
        }

        //  OTP send limit check
        if (userOtp.getOtpSendAttempt() >= 5) {
            throw new RuntimeException("OTP sending limit exceeded. Try again after 20 minutes.");
        }

        //  ----- Resend OTP -----
        //  generate new OTP
        String otp = generateOtp();

        //  send OTP
        boolean otpSent = emailService.sendOtpEmail(otp, userEmail, purpose);

        //  replace new OTP cache with old if exists
        if (otpSent) {
            now = LocalDateTime.now();

            //  delete old OTP
            if (userOtp.getOtpCache() != null) {
                otpCacheRepo.delete(userOtp.getOtpCache());
            }

            //  store new OTP
            otpCacheRepo.save(OtpCache.builder()
                    .userOtp(userOtp)
                    .otpHash(hashOtp(otp))
                    .otpVerifyAttempt(0)
                    .createdAt(now)
                    .expiresAt(now.plusSeconds(300))
                    .build());

            //  --- update UserOtp ---
            userOtp.setLastSentAt(now);

            //  update OTP send attempt (max 5)
            userOtp.setOtpSendAttempt(userOtp.getOtpSendAttempt() < 5
                    ? userOtp.getOtpSendAttempt() + 1
                    : 1);

            //  update block until time (20 min for 5th OTP send & 30 sec for others)
            userOtp.setBlockedUntil(userOtp.getOtpSendAttempt() < 5
                    ? now.plusSeconds(30)
                    : now.plusMinutes(20));

            userOtpRepo.save(userOtp);

            return true;
        } else {
            logger.warn("Resend OTP failed for user: {}, OTP not sent.", userEmail);
            return false;
        }
    }


    //  6-digit OTP generator
    private String generateOtp() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }


    //  hash OTP - using Auth-Service
    private String hashOtp(String otp) {
        ResponseEntity<HashDto> response = authFeign.hashOtp(new HashDto(otp));
        if (response == null || response.getBody() == null) {
            throw new RuntimeException("Failed to hash OTP.");
        }
        return response.getBody().getString();
    }

}
