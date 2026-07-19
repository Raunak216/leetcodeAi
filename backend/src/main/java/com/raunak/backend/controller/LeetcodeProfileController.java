package com.raunak.backend.controller;

import com.raunak.backend.dto.LeetcodeProfileRequest;
import com.raunak.backend.model.LeetcodeProfile;
import com.raunak.backend.security.AuthUser;
import com.raunak.backend.service.LeetcodeProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class LeetcodeProfileController {
    public final LeetcodeProfileService leetcodeProfileService;

    public LeetcodeProfileController(LeetcodeProfileService leetcodeProfileService) {
        this.leetcodeProfileService = leetcodeProfileService;
    }

    @PostMapping
    public LeetcodeProfile createProfile(@Valid @RequestBody LeetcodeProfileRequest request) {

        return leetcodeProfileService.saveProfile(request);
    }

    @GetMapping("/me")
    public LeetcodeProfile getLeetcodeProfile(
            Authentication authentication
    ) {

        AuthUser authUser =
                (AuthUser) authentication.getPrincipal();

        return leetcodeProfileService.getLeetcodeProfile(
                authUser.getUserId()
        );
    }

}
