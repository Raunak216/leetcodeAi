package com.raunak.backend.controller;

import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import com.raunak.backend.security.AuthUser;
import com.raunak.backend.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public User me() {

        AuthUser authUser =
                (AuthUser) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userRepository
                .findById(authUser.getUserId())
                .orElseThrow();
    }

    @GetMapping("/extension-token")
    public Map<String, String> extensionToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        int userId =
                jwtService.getUserIdFromRequest(
                        request
                );

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow();

        String jwt = jwtService.generateToken(user);

        Cookie cookie = new Cookie(
                "unSheet",
                jwt
        );

        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);

        response.addCookie(cookie);

        return Map.of(
                "token",
                jwt
        );

    }

    @PostMapping("/logout")
    public void logout(
            HttpServletResponse response
    ) {

        Cookie cookie =
                new Cookie(
                        "unSheet",
                        ""
                );

        cookie.setMaxAge(
                0
        );

        cookie.setPath(
                "/"
        );

        response.addCookie(
                cookie
        );

    }

}
