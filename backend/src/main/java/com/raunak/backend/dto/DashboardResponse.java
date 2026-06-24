package com.raunak.backend.dto;

import com.raunak.backend.model.QuestionAttempt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private int totalAttempts;

    private int questionsSolved;

    private int analyzedAttempts;

    private List<QuestionAttempt> recentAttempts;
}