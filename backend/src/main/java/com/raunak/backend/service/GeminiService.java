package com.raunak.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.config.GeminiConfig;
import com.raunak.backend.dto.AnalysisResult;
import com.raunak.backend.model.QuestionAttempt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final GeminiConfig geminiConfig;

    private final ObjectMapper objectMapper;
    private final PromptBuilderService promptBuilderService;
    public GeminiService(
            GeminiConfig geminiConfig,
            ObjectMapper objectMapper,
            PromptBuilderService promptBuilderService
    ) {
        this.geminiConfig = geminiConfig;
        this.objectMapper = objectMapper;
        this.promptBuilderService= promptBuilderService;
    }
    private WebClient webClient() {

        return WebClient.builder()
                .baseUrl(
                        "https://generativelanguage.googleapis.com"
                )
                .build();
    }

    public AnalysisResult analyze(
            QuestionAttempt attempt
    ) {
        String prompt =
                promptBuilderService
                        .buildPrompt(attempt);

        Map<String,Object> body =
                Map.of(
                        "contents",
                        List.of(
                                Map.of(
                                        "parts",
                                        List.of(
                                                Map.of(
                                                        "text",
                                                        prompt
                                                )
                                        )
                                )
                        )
                );
        String response =
                webClient()
                        .post()
                        .uri(
                                "/v1beta/models/gemini-2.5-flash:generateContent?key="
                                        + geminiConfig.getApiKey()
                        )
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

        try {

            JsonNode root =
                    objectMapper.readTree(
                            response
                    );

            String geminiText =
                    root
                            .path("candidates")
                            .get(0)
                            .path("content")
                            .path("parts")
                            .get(0)
                            .path("text")
                            .asText();

            geminiText =
                    geminiText
                            .replace(
                                    "```json",
                                    ""
                            )
                            .replace(
                                    "```",
                                    ""
                            )
                            .trim();

            AnalysisResult result =
                    objectMapper.readValue(
                            geminiText,
                            AnalysisResult.class
                    );
            System.out.println(
                    objectMapper.writeValueAsString(
                            result
                    )
            );
            return result;

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }
}