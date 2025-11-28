package com.example.telefonbozor.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.token}")
    private String token;

    @Value("${jwt.expiry}")
    private long expirationTime;

    public String generateToken(String phoneNumber,String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationTime);
        return Jwts.builder()
                .subject(phoneNumber)
                .claim("role",role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSignInKey(token))
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey(this.token))
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String extractSubject(String token){
        return Jwts.parser()
                .verifyWith(getSignInKey(this.token))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey getSignInKey(String token) {
        return Keys.hmacShaKeyFor(token.getBytes());
    }
}
