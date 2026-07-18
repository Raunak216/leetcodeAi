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
    private String difficulty;
    private String language;
    private Integer runtime;
    private Integer memory;
    private String journeyJson;
    
}