package com.menzo.Identity_Service.Service;

import com.menzo.Identity_Service.Dto.EmailDto;
import com.menzo.Identity_Service.Dto.User;
import com.menzo.Identity_Service.Feign.UserFeign;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

    @Autowired
    private UserFeign userFeign;

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public void validateToken(final String token){
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    public String generateToken(String userEmail){
        User user = userFeign.getUserbyUserEmail(new EmailDto(userEmail));
        Map<String, Object> claims = Map.of("roles", user.getRoles());
        return createToken(claims, userEmail);
    }

    private String createToken(Map<String, Object> claims, String userEmail){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
