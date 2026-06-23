package com.raunak.backend.controller;

import com.raunak.backend.dto.RecommendationResponse;
import com.raunak.backend.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/general/{userId}")
    public RecommendationResponse
    getRecommendations(
            @PathVariable int userId
    ) {

        return recommendationService
                .getGeneralRecommendations(
                        userId
                );
    }
}