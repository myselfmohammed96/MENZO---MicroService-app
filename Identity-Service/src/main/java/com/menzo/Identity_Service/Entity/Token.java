package com.menzo.Identity_Service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Column(name = "token")
    private String token;

    @Column(name = "is_logged_out")
    private boolean loggedOut;

    @Column(name = "user_id")
    private Long userId;

    //////////////////////////////////////

    public Token(String token, boolean loggedOut, Long userId){
        this.token = token;
        this.loggedOut = loggedOut;
        this.userId = userId;
    }

}
