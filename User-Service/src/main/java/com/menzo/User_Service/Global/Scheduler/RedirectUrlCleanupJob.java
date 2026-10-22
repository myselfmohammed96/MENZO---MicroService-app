package com.menzo.User_Service.Global.Scheduler;

import com.menzo.User_Service.Global.Cache.Repository.RedirectUrlTempRepository;
import com.menzo.User_Service.Global.Config.SignupProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedirectUrlCleanupJob {

    private final RedirectUrlTempRepository repo;
    private final SignupProperties signupProperties;

    @Scheduled(cron = "${app.signup.cleanup-cron:0 30 * * * *}")
    @Transactional
    public void purgeStale() {
        int removed = repo.deleteOlderThan(LocalDateTime.now().minus(signupProperties.redirectTtl()));
        log.info("Redirect cleanup removed {} stale rows", removed);
    }
}
