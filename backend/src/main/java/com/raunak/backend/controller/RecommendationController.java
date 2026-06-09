package com.raunak.backend.controller;

import com.raunak.backend.dto.RecommendationResponse;
import com.raunak.backend.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {


    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService){
        this.recommendationService = recommendationService;
    }

    @GetMapping("/user/{userId}")
    public RecommendationResponse
    getRecommendation(@PathVariable int userId){
        return recommendationService.getRecommendation(userId);
    }
}
