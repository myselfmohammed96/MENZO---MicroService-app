package com.menzo.User_Service.Cache.TempDbCache.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.menzo.User_Service.Cache.TempDbCache.DirectedTo;
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
@Table(name = "redirect_url_temp")
public class RedirectUrlTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID urlId;

    @Column(nullable = false)
    private String redirectUrl;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private DirectedTo directedTo;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
