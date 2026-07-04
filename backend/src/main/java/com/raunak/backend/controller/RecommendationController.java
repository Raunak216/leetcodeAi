package com.raunak.backend.controller;

import com.raunak.backend.dto.GeneralRecommendationRequest;
import com.raunak.backend.dto.RecommendationResponse;
import com.raunak.backend.security.AuthUser;
import com.raunak.backend.service.RecommendationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.raunak.backend.dto.CompanyRecommendationRequest;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService
    ) {
        this.recommendationService =
                recommendationService;
    }

    @PostMapping("/general")
    public RecommendationResponse getRecommendations(

            Authentication authentication,

            @RequestBody
            GeneralRecommendationRequest request
    ){

        AuthUser authUser =
                (AuthUser) authentication.getPrincipal();

        return recommendationService
                .getGeneralRecommendations(

                        authUser.getUserId(),

                        request
                );
    }
    @PostMapping("/company")
    public RecommendationResponse getCompanyRecommendations(
            Authentication authentication,

            @RequestBody
            CompanyRecommendationRequest request
    ){

        AuthUser authUser =
                (AuthUser) authentication.getPrincipal();

        return recommendationService.getCompanyRecommendations(
                authUser.getUserId(),
                request
        );
    }
}