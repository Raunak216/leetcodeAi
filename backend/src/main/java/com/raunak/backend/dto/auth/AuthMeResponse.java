package com.raunak.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthMeResponse {

    private int userId;

    private String userName;

    private String email;

    private String leetcodeUsername;

    private boolean leetcodeVerified;
}