package com.menzo.Identity_Service.Auth.Service;

import com.menzo.Identity_Service.Auth.Entity.LoginAttempt;
import com.menzo.Identity_Service.Auth.Repository.LoginAttemptRepository;
import com.menzo.Identity_Service.Global.Config.SecurityProperties;
import com.menzo.Identity_Service.Global.Exception.AuthException;
import com.menzo.Identity_Service.Global.Exception.ErrorCode;
import com.menzo.Identity_Service.Global.util.MaskingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private final LoginAttemptRepository repo;
    private final SecurityProperties properties;
    private final Clock clock;

    @Transactional(readOnly = true)
    public void assertNotLocked(String email) {
        Instant now = Instant.now(clock);
        repo.findById(email)
                .filter(a -> a.getLockedUntil() != null && now.isBefore(a.getLockedUntil()))
                .ifPresent(a -> {
                    long wait = Math.max(1, Duration.between(now, a.getLockedUntil()).toSeconds());
                    log.warn("Login blocked for {} ({}s left)", MaskingUtil.maskEmail(email), wait);
                    throw new AuthException(ErrorCode.TOO_MANY_LOGIN_ATTEMPTS,
                            "Too many failed login attempts. Please try again later.").withRetryAfter(wait);
                });
    }

    @Transactional
    public void recordFailure(String email) {
        Instant now = Instant.now(clock);
        LoginAttempt attempt = repo.findForUpdate(email).orElseGet(() -> {
            LoginAttempt created = new LoginAttempt();
            created.setEmail(email);
            return created;
        });

        boolean windowExpired = attempt.getFirstFailureAt() == null
                || now.isAfter(attempt.getFirstFailureAt().plus(properties.failureWindow()));
        boolean lockElapsed = attempt.getLockedUntil() != null && !now.isBefore(attempt.getLockedUntil());
        if (windowExpired || lockElapsed) {
            attempt.setFailedCount(0);
            attempt.setFirstFailureAt(now);
            attempt.setLockedUntil(null);
        }

        attempt.setFailedCount(attempt.getFailedCount() + 1);
        attempt.setLastFailureAt(now);
        if (attempt.getFailedCount() >= properties.maxFailedLogins()) {
            attempt.setLockedUntil(now.plus(properties.lockDuration()));
            log.warn("Too many failed logins, locking {} for {}", MaskingUtil.maskEmail(email), properties.lockDuration());
        }
        repo.save(attempt);
    }

    @Transactional
    public void reset(String email) {
        repo.findById(email).ifPresent(repo::delete);
    }
}
