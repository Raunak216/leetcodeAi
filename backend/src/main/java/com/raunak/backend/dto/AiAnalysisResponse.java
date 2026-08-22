package com.raunak.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AiAnalysisResponse {

    private double score;

    private List<String> tags;
}