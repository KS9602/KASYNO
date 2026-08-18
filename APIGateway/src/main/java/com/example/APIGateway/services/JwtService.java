package com.example.APIGateway.services;

import com.example.APIGateway.configs.ConfigProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    private final ConfigProperties configProperties;
    public JwtService(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                configProperties.getSecretKey()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .requireIssuer(configProperties.getIss())
                .requireAudience(configProperties.getAud())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

}
