package com.menzo.Identity_Service.Auth.Service;

import com.menzo.Identity_Service.Auth.Entity.UserTokenCutoff;
import com.menzo.Identity_Service.Auth.Repository.UserTokenCutoffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenRevocationService {

    private final UserTokenCutoffRepository repo;
    private final Clock clock;

    /** Every token issued before "now" stops working. JWT iat has second precision, so the cutoff is truncated to seconds. */
    @Transactional
    public void revokeAllFor(UUID userId) {
        UserTokenCutoff cutoff = repo.findById(userId).orElseGet(() -> {
            UserTokenCutoff created = new UserTokenCutoff();
            created.setUserId(userId);
            return created;
        });
        cutoff.setValidAfter(Instant.now(clock).truncatedTo(ChronoUnit.SECONDS));
        repo.save(cutoff);
        log.info("All earlier tokens revoked for userId={}", userId);
    }

    @Transactional(readOnly = true)
    public boolean isRevoked(UUID userId, Instant issuedAt) {
        return repo.findById(userId).map(c -> issuedAt.isBefore(c.getValidAfter())).orElse(false);
    }
}
