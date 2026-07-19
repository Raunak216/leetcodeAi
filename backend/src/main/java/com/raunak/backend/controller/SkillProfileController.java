package com.raunak.backend.controller;

import com.raunak.backend.model.SkillProfile;
import com.raunak.backend.security.AuthUser;
import com.raunak.backend.service.SkillProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skills")
public class SkillProfileController {

    private final SkillProfileService skillProfileService;

    public SkillProfileController(
            SkillProfileService skillProfileService
    ) {
        this.skillProfileService =
                skillProfileService;
    }

    @GetMapping("/me")
    public SkillProfile getSkills(
            Authentication authentication
    ) {

        AuthUser authUser =
                (AuthUser) authentication.getPrincipal();

        return skillProfileService.getProfile(
                authUser.getUserId()
        );
    }
}