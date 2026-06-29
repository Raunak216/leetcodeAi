package com.raunak.backend.controller;

import com.raunak.backend.dto.auth.AuthMeResponse;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.UserRepository;
import com.raunak.backend.security.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(
            UserRepository userRepository
    ){
        this.userRepository=userRepository;
    }

    @GetMapping("/me")
    public AuthMeResponse me(
            Authentication authentication
    ) {

        AuthUser authUser =
                (AuthUser) authentication.getPrincipal();

        User user =
                userRepository
                        .findById(
                                authUser.getUserId()
                        )
                        .orElseThrow();

        return new AuthMeResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getLeetcodeUsername(),
                user.isLeetcodeVerified()
        );
    }
}