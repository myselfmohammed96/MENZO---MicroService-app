package com.menzo.Identity_Service.Global.Scheduler;

import com.menzo.Identity_Service.Auth.Repository.LoginAttemptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginAttemptCleanupJob {

    private final LoginAttemptRepository repo;
    private final Clock clock;

    @Scheduled(cron = "0 20 * * * *")
    @Transactional
    public void purgeStale() {
        int removed = repo.deleteStale(Instant.now(clock).minus(Duration.ofDays(1)));
        log.info("Login attempt cleanup removed {} rows", removed);
    }
}
