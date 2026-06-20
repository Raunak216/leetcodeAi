package com.raunak.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AiAnalysisResponse {

    private String summary;

    private Map<String,Integer> skillDelta;
}