package com.menzo.Communication_Service.Otp.Service;

import com.menzo.Communication_Service.Email.Service.EmailService;
import com.menzo.Communication_Service.Global.Config.OtpProperties;
import com.menzo.Communication_Service.Global.Dto.SendOtpRequest;
import com.menzo.Communication_Service.Global.Dto.SendOtpResponse;
import com.menzo.Communication_Service.Global.Dto.VerifyOtpRequest;
import com.menzo.Communication_Service.Global.Dto.VerifyOtpResponse;
import com.menzo.Communication_Service.Global.Enum.ErrorCode;
import com.menzo.Communication_Service.Global.Exception.BusinessException;
import com.menzo.Communication_Service.Global.Exception.OtpRateLimitException;
import com.menzo.Communication_Service.Global.Enum.Purpose;
import com.menzo.Communication_Service.Otp.Entity.OtpCache;
import com.menzo.Communication_Service.Otp.Entity.UserOtp;
import com.menzo.Communication_Service.Otp.Repository.OtpCacheRepository;
import com.menzo.Communication_Service.Otp.Repository.UserOtpRepository;
import com.menzo.Communication_Service.Otp.Util.MaskingUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    private final UserOtpRepository userOtpRepo;

    private final OtpCacheRepository otpCacheRepo;

    private final EmailService emailService;

    private final OtpProperties properties;

    private final Clock clock;

    private final OtpUtil otpUtil;


    @Transactional(noRollbackFor = OtpRateLimitException.class)
    public SendOtpResponse sendOtp(SendOtpRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        Purpose purpose = request.purpose();
        String masked = MaskingUtil.maskEmail(email);
        LocalDateTime now = LocalDateTime.now(clock);
        logger.info("OTP send requested, email={}, purpose={}", masked, purpose);

        UserOtp userOtp = userOtpRepo.findForUpdate(email, purpose)
                .orElseGet(() -> newUserOtp(email, purpose));
        enforceSendLimits(userOtp, now, masked);

        String otp = otpUtil.generateOtp();

        userOtp.setOtpSendAttempt(userOtp.getOtpSendAttempt() + 1);
        userOtp.setLastSentAt(now);
        userOtp = userOtpRepo.saveAndFlush(userOtp);   // flush -> unique-constraint races surface BEFORE we email

        OtpCache cache = userOtp.getOtpCache() != null ? userOtp.getOtpCache() : new OtpCache();
        cache.setUserOtp(userOtp);
        cache.setOtpHash(otpUtil.hashOtp(email, purpose, otp));
        cache.setOtpVerifyAttempt(0);                          // fresh code -> fresh verify budget
        cache.setExpiresAt(now.plus(properties.expiry()));
        otpCacheRepo.save(cache);
        userOtp.setOtpCache(cache);

        emailService.sendOtpEmail(email, request.firstName(), otp, purpose, properties.expiry());

        logger.info("OTP issued, email={}, sendAttempt={}", masked, userOtp.getOtpSendAttempt());
        return new SendOtpResponse("OTP_SENT",
                properties.expiry().toSeconds(),
                properties.resendCooldown().toSeconds());
    }

    @Transactional(noRollbackFor = BusinessException.class)
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        Purpose purpose = request.purpose();
        String masked = MaskingUtil.maskEmail(email);
        LocalDateTime now = LocalDateTime.now(clock);
        logger.info("OTP verification requested, email={}, purpose={}", masked, purpose);

        UserOtp userOtp = userOtpRepo.findForUpdate(email, purpose)
                .orElseThrow(() -> noActiveOtp(masked));
        OtpCache cache = userOtp.getOtpCache();
        if (cache == null) {
            throw noActiveOtp(masked);
        }

        if (!now.isBefore(cache.getExpiresAt())) {
            discard(userOtp, cache);
            logger.warn("OTP expired for {}", masked);
            throw new BusinessException(ErrorCode.OTP_EXPIRED, "This OTP has expired. Please request a new one.");
        }

        int max = properties.maxVerifyAttempts();
        if (cache.getOtpVerifyAttempt() >= max) {
            logger.warn("OTP verify limit already reached for {}", masked);
            throw verifyLimitExceeded();
        }

        if (!otpUtil.otpMatches(email, purpose, request.otp(), cache.getOtpHash())) {
            int used = cache.getOtpVerifyAttempt() + 1;
            cache.setOtpVerifyAttempt(used);
            otpCacheRepo.save(cache);
            int remaining = max - used;
            logger.warn("Incorrect OTP for {}, attempt {}/{}", masked, used, max);
            if (remaining <= 0) {
                throw verifyLimitExceeded();
            }
            throw new BusinessException(ErrorCode.OTP_INVALID,
                    "Incorrect OTP. " + remaining + " attempt(s) remaining.")
                    .withErrors(Map.of("remainingAttempts", String.valueOf(remaining)));
        }

        discard(userOtp, cache);                 // single use: a verified OTP can never be replayed
        userOtp.setOtpSendAttempt(0);
        userOtp.setBlockedUntil(null);
        userOtpRepo.save(userOtp);
        logger.info("OTP verified for {}", masked);
        return new VerifyOtpResponse("OTP_VERIFIED");
    }

    private void discard(UserOtp userOtp, OtpCache cache) {
        otpCacheRepo.delete(cache);
        userOtp.setOtpCache(null);
    }

    private BusinessException noActiveOtp(String masked) {
        logger.warn("No active OTP for {}", masked);
        return new BusinessException(ErrorCode.OTP_NOT_FOUND, "No active OTP found. Please request a new one.");
    }

    private BusinessException verifyLimitExceeded() {
        return new BusinessException(ErrorCode.OTP_VERIFY_LIMIT_EXCEEDED,
                "Too many incorrect attempts. Please request a new OTP.");
    }

    private UserOtp newUserOtp(String email, Purpose purpose) {
        UserOtp userOtp = new UserOtp();
        userOtp.setUserEmail(email);
        userOtp.setPurpose(purpose);
        userOtp.setOtpSendAttempt(0);
        return userOtp;
    }

    private void enforceSendLimits(UserOtp userOtp, LocalDateTime now, String masked) {
        if (userOtp.getBlockedUntil() != null) {
            if (now.isBefore(userOtp.getBlockedUntil())) {
                long wait = secondsUntil(now, userOtp.getBlockedUntil());
                logger.warn("OTP request rejected, {} is blocked for another {}s", masked, wait);
                throw new OtpRateLimitException("Too many OTP requests. Please try again later.", wait);
            }
            userOtp.setBlockedUntil(null);   // block elapsed
            userOtp.setOtpSendAttempt(0);
        }

        if (userOtp.getLastSentAt() != null) {
            Duration sinceLast = Duration.between(userOtp.getLastSentAt(), now);
            if (sinceLast.compareTo(properties.resendCooldown()) < 0) {
                long wait = secondsUntil(now, userOtp.getLastSentAt().plus(properties.resendCooldown()));
                logger.warn("OTP request rejected, resend cooldown active for {} ({}s left)", masked, wait);
                throw new OtpRateLimitException("Please wait before requesting another OTP.", wait);
            }
            if (sinceLast.compareTo(properties.attemptWindow()) > 0) {
                userOtp.setOtpSendAttempt(0);   // old activity falls out of the window
            }
        }

        if (userOtp.getOtpSendAttempt() >= properties.maxSendAttempts()) {
            userOtp.setBlockedUntil(now.plus(properties.blockDuration()));
            userOtpRepo.save(userOtp);
            logger.warn("OTP send limit reached, blocking {} for {}", masked, properties.blockDuration());
            throw new OtpRateLimitException("Too many OTP requests. Please try again later.",
                    properties.blockDuration().toSeconds());
        }
    }

    private long secondsUntil(LocalDateTime from, LocalDateTime to) {
        return Math.max(1, (long) Math.ceil(Duration.between(from, to).toMillis() / 1000.0));
    }


//    ---------------------------------------------

    /*
     *
     *   Send new OTP
     *
     */
//    @Transactional
//    public boolean sendNewOtp(EmailDto userEmail, Purpose purpose) {
//
//        String email = userEmail.getEmail().trim().toLowerCase(Locale.ROOT);
//
//
//        //  get UserOtp by userEmail and purpose if exists
//        Optional<UserOtp> existingUserOtp = userOtpRepo.findByUserEmailAndPurpose(userEmail, purpose);
//
//        boolean userOtpExists = existingUserOtp.isPresent();
//
//        UserOtp userOtp = existingUserOtp.orElseGet(() -> userOtpRepo.save(
//                UserOtp.builder()
//                        .userEmail(userEmail)
//                        .purpose(purpose)
//                        .build()
//        ));
//
//        //  ----- Sanitize & validate old UserOtp object -----
//        LocalDateTime now = LocalDateTime.now();
//        boolean userOtpActive = false;
//
//        //  reset lastSentAt if userOtp not active
//        if (userOtpExists
//                && userOtp.getLastSentAt() != null
//                && userOtp.getLastSentAt().plusHours(12).isBefore(now)) {
//            userOtp.setLastSentAt(null);
//            userOtp.setOtpSendAttempt(0);
//            userOtp.setBlockedUntil(null);
//        } else {
//            userOtpActive = true;
//        }
//
//        if (userOtpActive) {
//            //  block time check
//            if (userOtp.getBlockedUntil() != null
//                    && userOtp.getBlockedUntil().isAfter(now)) {
//                throw new RuntimeException("OTP send is temporarily blocked. Please try again later.");
//            }
//
//            //  OTP send limit check
//            if (userOtp.getOtpSendAttempt() >= 5
//                    && userOtp.getBlockedUntil() != null
//                    && userOtp.getBlockedUntil().isAfter(now)) {
//                long remainingMinutes = ChronoUnit.MINUTES.between(
//                        now,
//                        userOtp.getBlockedUntil()
//                );
//
//                if (now.plusMinutes(remainingMinutes).isBefore(userOtp.getBlockedUntil())) {
//                    remainingMinutes++;
//                    throw new RuntimeException("OTP sending limit exceeded. Try again after " + remainingMinutes + (remainingMinutes > 1 ? " minutes." : " minute."));
//                }
//            }
//        }
//
//        //  ----- Send OTP -----
//        String otp = generateOtp();
//
//        //  send OTP
//        boolean otpSent = emailService.sendOtpEmail(otp, userEmail, purpose);
//
//        if (otpSent) {
//            now = LocalDateTime.now();
//
//            //  delete old OTP cache
//            if (userOtp.getOtpCache() != null) {
//                otpCacheRepo.delete(userOtp.getOtpCache());
//            }
//
//            //  store new OTP
//            otpCacheRepo.save(OtpCache.builder()
//                    .userOtp(userOtp)
//                    .otpHash(hashOtp(otp))
//                    .otpVerifyAttempt(0)
//                    .createdAt(now)
//                    .expiresAt(now.plusSeconds(300))
//                    .build());
//
//            //  --- update UserOtp ---
//            userOtp.setLastSentAt(now);
//
//            //  update OTP send attempt (max 5)
//            userOtp.setOtpSendAttempt(userOtp.getOtpSendAttempt() < 5
//                    ? userOtp.getOtpSendAttempt() + 1
//                    : 1);
//
//            //  update block until time (20 min for 5th OTP send & 30 sec for others)
//            userOtp.setBlockedUntil(userOtp.getOtpSendAttempt() < 5
//                    ? now.plusSeconds(30)
//                    : now.plusMinutes(20));
//
//            userOtpRepo.save(userOtp);
//
//            return true;
//        } else {
//            logger.warn("Failed to send OTP for user : {}, OTP not sent.", userEmail);
//            return false;
//        }
//
//    }


    /*
     *
     *   Resend OTP
     *
     */
//    @Transactional
//    public boolean resendOtp(String userEmail, Purpose purpose) {
//        //  get UserOtp by userEmail and purpose if exists
//        UserOtp userOtp = userOtpRepo.findByUserEmailAndPurpose(userEmail, purpose)
//                .orElseThrow(() -> new IllegalArgumentException("No active OTP verification process found."));
//
//        //  ----- UserOtp validation -----
//        LocalDateTime now = LocalDateTime.now();
//
//        //  overall process lifetime check
//        if (userOtp.getLastSentAt() != null
//                && userOtp.getLastSentAt().plusHours(12).isBefore(now)) {
//            throw new IllegalArgumentException("OTP verification process has expired. Please start again.");
//        }
//
//        //  block time check
//        if (userOtp.getBlockedUntil() != null
//                && userOtp.getBlockedUntil().isAfter(now)) {
//            throw new RuntimeException("OTP resend is temporarily blocked. Please try again later.");
//        }
//
//        //  OTP send limit check
//        if (userOtp.getOtpSendAttempt() >= 5
//                && userOtp.getBlockedUntil() != null
//                && userOtp.getBlockedUntil().isAfter(now)) {
//            long remainingMinutes = ChronoUnit.MINUTES.between(
//                    now,
//                    userOtp.getBlockedUntil()
//            );
//
//            if (now.plusMinutes(remainingMinutes).isBefore(userOtp.getBlockedUntil())) {
//                remainingMinutes++;
//                throw new RuntimeException("OTP sending limit exceeded. Try again after " + remainingMinutes + (remainingMinutes > 1 ? " minutes." : "minute."));
//            }
//        }
//
//        //  ----- Resend OTP -----
//        //  generate new OTP
//        String otp = generateOtp();
//
//        //  send OTP
//        boolean otpSent = emailService.sendOtpEmail(otp, userEmail, purpose);
//
//        //  replace new OTP cache with old if exists
//        if (otpSent) {
//            now = LocalDateTime.now();
//
//            //  delete old OTP
//            if (userOtp.getOtpCache() != null) {
//                otpCacheRepo.delete(userOtp.getOtpCache());
//            }
//
//            //  store new OTP
//            otpCacheRepo.save(OtpCache.builder()
//                    .userOtp(userOtp)
//                    .otpHash(hashOtp(otp))
//                    .otpVerifyAttempt(0)
//                    .createdAt(now)
//                    .expiresAt(now.plusSeconds(300))
//                    .build());
//
//            //  --- update UserOtp ---
//            userOtp.setLastSentAt(now);
//
//            //  update OTP send attempt (max 5)
//            userOtp.setOtpSendAttempt(userOtp.getOtpSendAttempt() < 5
//                    ? userOtp.getOtpSendAttempt() + 1
//                    : 1);
//
//            //  update block until time (20 min for 5th OTP send & 30 sec for others)
//            userOtp.setBlockedUntil(userOtp.getOtpSendAttempt() < 5
//                    ? now.plusSeconds(30)
//                    : now.plusMinutes(20));
//
//            userOtpRepo.save(userOtp);
//
//            return true;
//        } else {
//            logger.warn("Resend OTP failed for user: {}, OTP not sent.", userEmail);
//            return false;
//        }
//    }

}
