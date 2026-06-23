package com.raunak.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendationResponse {

    private List<String> recommendedQuestions;
    private String reasoning;
}