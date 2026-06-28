package com.raunak.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {

        key = Keys.hmacShaKeyFor(
                secret.getBytes(
                        StandardCharsets.UTF_8
                )
        );
    }

    public String generateToken(
            int userId
    ) {

        return Jwts.builder()
                .subject(
                        String.valueOf(
                                userId
                        )
                )
                .issuedAt(
                        new Date()
                )
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000L * 60 * 60 * 24 * 30
                        )
                )
                .signWith(
                        key
                )
                .compact();
    }

    public int getUserId(
            String token
    ) {

        Claims claims =
                Jwts.parser()
                        .verifyWith(
                                key
                        )
                        .build()
                        .parseSignedClaims(
                                token
                        )
                        .getPayload();

        return Integer.parseInt(
                claims.getSubject()
        );
    }

    public boolean isValid(
            String token
    ) {

        try {

            Jwts.parser()
                    .verifyWith(
                            key
                    )
                    .build()
                    .parseSignedClaims(
                            token
                    );

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}