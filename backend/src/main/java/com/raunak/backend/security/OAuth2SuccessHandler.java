package com.raunak.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.auth.AuthResponse;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import com.raunak.backend.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    @Value("${frontend.url}")
    private String frontendUrl;

    public OAuth2SuccessHandler(
            UserRepository userRepository,
            JwtService jwtService,
            ObjectMapper objectMapper,
            UserService userService
    ) {

        this.userRepository =
                userRepository;

        this.jwtService =
                jwtService;

        this.objectMapper =
                objectMapper;
        this.userService = userService;
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

                            return userService.saveUser(newUser);
                        });

        String jwt =
                jwtService.generateToken(
                        user
                );


        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }


        Cookie cookie =
                new Cookie(
                        "algolens_jwt",
                        jwt
                );

        cookie.setHttpOnly(
                true
        );

        cookie.setSecure(
                request.isSecure()
        );

        cookie.setPath(
                "/"
        );

        cookie.setMaxAge(
                60 * 60 * 24 * 30
        );

        response.addCookie(
                cookie
        );

        response.sendRedirect(
                frontendUrl +
                        "/auth/success"
        );
    }
}