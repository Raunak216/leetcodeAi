package com.raunak.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRecommendationRequest {

    private Integer userId;

    private String company;

    private int daysRemaining;
}