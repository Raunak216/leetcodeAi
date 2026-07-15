package com.raunak.backend.controller;

import com.raunak.backend.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/extension-token")
    public Map<String, String> getToken(
            HttpServletRequest request
    ) {

        Cookie[] cookies =
                request.getCookies();

        if (cookies == null) {
            throw new RuntimeException();
        }

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("algolens_jwt")) {

                return Map.of(

                        "token",

                        cookie.getValue()

                );

            }

        }

        throw new RuntimeException(
                "No Cookie"
        );
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletResponse response
    ) {

        Cookie cookie =
                new Cookie(
                        "algolens_jwt",
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
