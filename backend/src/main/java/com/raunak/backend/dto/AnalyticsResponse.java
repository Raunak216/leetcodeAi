package com.raunak.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AnalyticsResponse {

    private long totalEvents;
    private long accepted;
    private long wrongAnswers;
    private double acceptanceRate;
    private double averageTimeSpent;

    private String mostAttemptedProblem;
    private int maxAttempts;
}
