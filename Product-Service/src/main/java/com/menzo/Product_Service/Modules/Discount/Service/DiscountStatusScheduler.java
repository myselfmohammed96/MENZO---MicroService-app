package com.menzo.Product_Service.Modules.Discount.Service;

import com.menzo.Product_Service.Modules.Discount.Repo.DiscountRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DiscountStatusScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DiscountStatusScheduler.class);

    @Autowired
    private final DiscountRepo discountRepo;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void updateDiscountStatusesDbScheduling() {
        LocalDateTime now = LocalDateTime.now();

        try {
            //  SCHEDULED -> ACTIVE
            int activated = discountRepo.activateScheduled(now);

            //  ACTIVE | INACTIVE | PAUSED -> EXPIRED
            int expired = discountRepo.expireActive(now);

            //  PAUSED -> SCHEDULED | ACTIVE
            int resumed = discountRepo.resumePaused(now);

            if (activated + expired + resumed > 0) {
                logger.info(
                        "Discount status updated -> activated={}, resumed={}, expired={}", activated, resumed, expired);
            }
        } catch (Exception e) {
            logger.error("Discount status scheduler failed", e);
            //  ## add monitoring/alerts - logs, metrics, error tracking(Sentry, Prometheus)
        }
    }
}
