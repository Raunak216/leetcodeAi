package com.raunak.backend.security;

import com.raunak.backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = null;

        String header =
                request.getHeader("Authorization");

        System.out.println(
                "AUTH REQUEST: " +
                        request.getMethod() +
                        " " +
                        request.getRequestURI()
        );

        System.out.println(
                "AUTH HEADER: " +
                        header
        );

        Cookie[] cookies =
                request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                System.out.println(
                        "COOKIE: " +
                                cookie.getName()
                );

                if (cookie.getName().equals("unSheet")) {

                    token = cookie.getValue();

                    System.out.println(
                            "UNSHEET COOKIE FOUND"
                    );

                    break;
                }
            }
        }

        if (token != null) {

            boolean valid =
                    jwtService.isValid(token);

            System.out.println(
                    "JWT VALID: " +
                            valid
            );

            if (valid) {

                int userId =
                        jwtService.getUserId(token);

                System.out.println(
                        "JWT USER ID: " +
                                userId
                );

                boolean exists =
                        userRepository.existsById(userId);

                System.out.println(
                        "USER EXISTS: " +
                                exists
                );

                if (exists) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    new AuthUser(userId),
                                    null,
                                    AuthorityUtils.NO_AUTHORITIES
                            );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );

                    System.out.println(
                            "AUTHENTICATION SET"
                    );
                }
            }
        }

        System.out.println(
                "AUTHENTICATED: " +
                        (SecurityContextHolder
                                .getContext()
                                .getAuthentication() != null)
        );

        filterChain.doFilter(
                request,
                response
        );
    }
}