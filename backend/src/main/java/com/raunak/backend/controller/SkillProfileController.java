package com.raunak.backend.controller;

import com.raunak.backend.model.UserSkill;
import com.raunak.backend.security.AuthUser;
import com.raunak.backend.service.SkillProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<UserSkill> getSkills(
            Authentication authentication
    ) {

        AuthUser authUser =
                (AuthUser)
                        authentication.getPrincipal();

        return skillProfileService.getSkills(
                authUser.getUserId()
        );
    }
}