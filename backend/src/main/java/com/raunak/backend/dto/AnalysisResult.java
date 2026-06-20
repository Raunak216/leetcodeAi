package com.raunak.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AnalysisResult {

    private String summary;

    private Map<String,String> dsaSignals;
    private Map<String,String> engineeringSignals;
    private Map<String,String> reasoningSignals;
}