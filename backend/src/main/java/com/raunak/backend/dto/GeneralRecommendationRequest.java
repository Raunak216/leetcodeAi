package com.raunak.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralRecommendationRequest {

    private boolean interviewScheduled;

    private Integer daysRemaining;
}