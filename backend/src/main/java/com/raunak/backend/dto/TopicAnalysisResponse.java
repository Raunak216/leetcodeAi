package com.raunak.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopicAnalysisResponse {

    private String topic;
    private int questionsSolved;
    private double averageAttempts;
}