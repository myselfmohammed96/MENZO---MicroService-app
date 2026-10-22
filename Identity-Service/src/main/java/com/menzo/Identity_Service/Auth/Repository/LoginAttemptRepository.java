package com.menzo.Identity_Service.Auth.Repository;

import com.menzo.Identity_Service.Auth.Entity.LoginAttempt;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from LoginAttempt l where l.email = :email")
    Optional<LoginAttempt> findForUpdate(@Param("email") String email);

    @Modifying
    @Query("delete from LoginAttempt l where l.lastFailureAt < :cutoff and (l.lockedUntil is null or l.lockedUntil < :cutoff)")
    int deleteStale(@Param("cutoff") Instant cutoff);
}
