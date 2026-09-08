package com.menzo.Communication_Service.Otp.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "otp_cache",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_otp",
                columnNames = "user_otp_id"
        )
)
public class OtpCache {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID otpId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_otp_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_otp")
    )
    private UserOtp userOtp;

    @Column(nullable = false)
    private String otpHash;

    @Column(nullable = false)
    private Integer otpVerifyAttempt = 0;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime expiresAt;

}
