package com.raunak.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.auth.AuthResponse;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final ObjectMapper objectMapper;

    public OAuth2SuccessHandler(
            UserRepository userRepository,
            JwtService jwtService,
            ObjectMapper objectMapper
    ) {

        this.userRepository =
                userRepository;

        this.jwtService =
                jwtService;

        this.objectMapper =
                objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauthUser =
                (OAuth2User)
                        authentication.getPrincipal();

        String email =
                oauthUser.getAttribute(
                        "email"
                );

        String name =
                oauthUser.getAttribute(
                        "name"
                );

        User user =
                userRepository
                        .findByEmail(
                                email
                        )
                        .orElseGet(() -> {

                            User newUser =
                                    new User();

                            newUser.setEmail(
                                    email
                            );

                            newUser.setUserName(
                                    name
                            );

                            newUser.setLeetcodeVerified(
                                    false
                            );

                            return userRepository
                                    .save(
                                            newUser
                                    );
                        });

        String jwt =
                jwtService.generateToken(
                        user.getId()
                );

        AuthResponse authResponse =
                new AuthResponse(
                        jwt,
                        user.getId(),
                        user.getUserName(),
                        user.getEmail(),
                        user.getLeetcodeUsername(),
                        user.isLeetcodeVerified()
                );

        response.setContentType(
                "application/json"
        );

        objectMapper.writeValue(
                response.getWriter(),
                authResponse
        );
    }
}