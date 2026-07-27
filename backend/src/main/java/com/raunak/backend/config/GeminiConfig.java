package com.raunak.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GeminiConfig {

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}