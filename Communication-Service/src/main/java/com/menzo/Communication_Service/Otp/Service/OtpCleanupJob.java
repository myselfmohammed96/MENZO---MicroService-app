package com.menzo.Communication_Service.Otp.Service;

import com.menzo.Communication_Service.Otp.Repository.OtpCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OtpCleanupJob {

    private final OtpCacheRepository otpCacheRepo;
    private final Clock clock;

    @Scheduled(cron = "${app.otp.cleanup-cron:0 0 * * * *}")
    @Transactional
    public void purgeExpired() {
        int removed = otpCacheRepo.deleteExpired(LocalDateTime.now(clock));
        log.info("OTP cleanup removed {} expired cache rows", removed);
    }
}
