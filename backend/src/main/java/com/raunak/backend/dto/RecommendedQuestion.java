package com.raunak.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecommendedQuestion {

    private String title;
    private String slug;
    private String difficulty;
    private List<String> topics;
    private String reason;
    private String estimatedTime;
}