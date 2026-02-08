package com.template.hexagonal.infrastructure.adapter.output.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.template.hexagonal.domain.exception.DomainException;
import com.template.hexagonal.domain.model.TokenParse;
import com.template.hexagonal.domain.port.output.security.TokenGenerator;
import com.template.hexagonal.infrastructure.config.JwtProperties;
import com.template.hexagonal.infrastructure.exception.AppError;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Adaptador de salida para generacion de tokens JWT
 * Implementa el puerto TokenGenerator
 */
@Component
@RequiredArgsConstructor
public class JwtToken implements TokenGenerator {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    private SecretKey getSecretKey() {
        if (secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        }
        return secretKey;
    }

    @Override
    public String generateToken(UUID userId, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString());
        claims.put("email", email);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSecretKey())
                .compact();
    }

    @Override
    public TokenParse parseToken(String token) {
        try {
            Map<String, Object> claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            String userId = (String) claims.get("userId");
            String email = (String) claims.get("email");
            return new TokenParse(UUID.fromString(userId), email);
        } catch (Exception e) {
            AppError error = AppError.of("JWT_PARSE_ERROR", "Error parsing JWT token", 401);
            throw new DomainException(error);
        }
    }
}
