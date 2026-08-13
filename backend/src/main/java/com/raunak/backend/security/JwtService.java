package com.raunak.backend.security;

import com.raunak.backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    public String generateToken(User user) {

        return Jwts.builder()

                .subject(
                        String.valueOf(
                                user.getId()
                        )
                )

                .claim(
                        "email",
                        user.getEmail()
                )

                .claim(
                        "name",
                        user.getUserName()
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

    public int getUserIdFromRequest(
            HttpServletRequest request
    ) {

        Cookie[] cookies =
                request.getCookies();

        if (cookies == null) {
            throw new RuntimeException("No Cookie");
        }

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("unSheet")) {

                return getUserId(
                        cookie.getValue()
                );

            }

        }

        throw new RuntimeException(
                "JWT Cookie Missing"
        );
    }
}