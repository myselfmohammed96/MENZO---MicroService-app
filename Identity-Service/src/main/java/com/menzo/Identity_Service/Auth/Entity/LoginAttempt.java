package com.menzo.Identity_Service.Auth.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "login_attempt")
public class LoginAttempt {

    @Id
    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private int failedCount;

    private Instant firstFailureAt;

    private Instant lastFailureAt;

    private Instant lockedUntil;

    @Version
    private Long version;
}
