package com.CptFranck.SportsPeak.config.security;

import com.CptFranck.SportsPeak.domain.model.JWToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JWToken generateToken(Authentication authentication) {
        String login = authentication.getName();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime validityDate = now.plusHours(1);
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date validityDateDate = Date.from(validityDate.atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
                .subject(login)
                .issuedAt(nowDate)
                .expiration(validityDateDate)
                .signWith(getSigningKey())
                .compact();

        return new JWToken(token, validityDate);
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT is not valid or expired");
        }
    }
}
