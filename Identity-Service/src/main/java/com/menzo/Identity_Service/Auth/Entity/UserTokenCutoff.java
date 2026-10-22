package com.menzo.Identity_Service.Auth.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_token_cutoff")
public class UserTokenCutoff {

    @Id
    private UUID userId;

    @Column(nullable = false)
    private Instant validAfter;
}
