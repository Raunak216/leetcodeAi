package com.raunak.backend.service;

import com.raunak.backend.dto.RecommendationResponse;
import com.raunak.backend.model.LeetcodeProfile;
import com.raunak.backend.repository.LeetcodeProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {
    private final LeetcodeProfileRepository leetcodeProfileRepository;

    public RecommendationService(LeetcodeProfileRepository repository){
        this.leetcodeProfileRepository = repository;
    }

    public RecommendationResponse getRecommendation(int userId){
        LeetcodeProfile profile = leetcodeProfileRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Profile not found"));

        if(profile.getHardSolved() < 50){
            return new RecommendationResponse(
                    "Graphs and Dynamic Programming",
                    "Low hard problem exposure detected."
            );
        }
        return new RecommendationResponse(
                "Advanced Problem Solving",
                "Continue practicing medium and hard problems."
        );
    }
}
