package com.raunak.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAttemptRequest {

    private String questionSlug;
    private String title;

    private String topic;
    private String difficulty;

    private int attempts;
    private int timeSpent;

    private boolean accepted;

    private int userId;
}