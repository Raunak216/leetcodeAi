package com.raunak.backend.security;

import com.raunak.backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.Cookie;

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

        if (
                header != null &&
                        header.startsWith("Bearer ")
        ) {

            token =
                    header.substring(7);

        }

        if (token == null) {

            Cookie[] cookies =
                    request.getCookies();

            if (cookies != null) {

                for (Cookie cookie : cookies) {

                    if (
                            cookie.getName()
                                    .equals("algolens_jwt")
                    ) {

                        token =
                                cookie.getValue();

                        break;
                    }
                }
            }
        }

        if (
                token != null &&
                        jwtService.isValid(token)
        ) {

            int userId =
                    jwtService.getUserId(token);

            if (
                    userRepository.existsById(userId)
            ) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                new AuthUser(userId),
                                null,
                                AuthorityUtils.NO_AUTHORITIES
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(
                request,
                response
        );

    }
}