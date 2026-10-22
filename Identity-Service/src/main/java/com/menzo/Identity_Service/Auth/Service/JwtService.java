package com.menzo.Identity_Service.Auth.Service;

import com.menzo.Identity_Service.Auth.Dto.AuthMethod;
import com.menzo.Identity_Service.Global.Config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class JwtService {

    private final JwtProperties properties;
    private final TokenRevocationService revocationService;
    private final Clock clock;
    private final Key key;

    public JwtService(JwtProperties properties, TokenRevocationService revocationService, Clock clock) {
        this.properties = properties;
        this.revocationService = revocationService;
        this.clock = clock;
        byte[] keyBytes = Decoders.BASE64.decode(properties.secret());
        if (keyBytes.length < 32) {
            throw new IllegalStateException("app.jwt.secret must decode to at least 32 bytes (256 bits)");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generate(String userId, String email, List<String> roles, AuthMethod method) {
        Instant now = Instant.now(clock);
        return Jwts.builder()
                .setClaims(Map.of("roles", roles, "uid", userId, "amr", method.name()))
                .setSubject(email)
                .setIssuer(properties.issuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(properties.ttl())))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Signature + expiry + issuer + "signed out everywhere" cutoff. Reusable by whatever validates tokens. */
    public Claims validate(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(properties.issuer())
                .setAllowedClockSkewSeconds(30)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String uid = claims.get("uid", String.class);
        if (uid != null && revocationService.isRevoked(UUID.fromString(uid), claims.getIssuedAt().toInstant())) {
            throw new JwtException("Token has been revoked");
        }
        return claims;
    }
}
