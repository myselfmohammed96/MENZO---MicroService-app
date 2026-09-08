package com.menzo.Communication_Service.Otp.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.Communication_Service.Global.Enum.Purpose;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "manage_user_otp",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_otp_purpose",
                columnNames = { "user_email", "purpose" }
        )
)
public class UserOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userOtpId;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private Purpose purpose;

    @Column(nullable = false)
    private Integer otpSendAttempt = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime lastSentAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime blockedUntil;

    @OneToOne(mappedBy = "userOtp")
    private OtpCache otpCache;

}
