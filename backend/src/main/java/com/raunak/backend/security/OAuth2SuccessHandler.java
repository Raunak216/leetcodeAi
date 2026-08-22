package com.raunak.backend.security;

import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import com.raunak.backend.service.UserService;
import jakarta.servlet.ServletException;
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

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    public OAuth2SuccessHandler(
            UserRepository userRepository,
            JwtService jwtService,
            UserService userService
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        String email =
                oauthUser.getAttribute("email");

        String name =
                oauthUser.getAttribute("name");

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseGet(() -> {

                            User newUser = new User();

                            newUser.setEmail(email);
                            newUser.setUserName(name);
                            newUser.setLeetcodeVerified(false);

                            return userService.saveUser(newUser);
                        });

        String jwt =
                jwtService.generateToken(user);

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        String cookie =
                "unSheet=" + jwt +
                        "; Path=/" +
                        "; Max-Age=" + (60 * 60 * 24 * 30) +
                        "; HttpOnly";

        if ("prod".equalsIgnoreCase(activeProfile)) {
            cookie += "; Secure; SameSite=None";
        } else {
            cookie += "; SameSite=Lax";
        }

        response.addHeader(
                "Set-Cookie",
                cookie
        );

        response.sendRedirect(
                frontendUrl + "/auth/success"
        );
    }
}