package com.raunak.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.config.GeminiConfig;
import com.raunak.backend.dto.AiAnalysisResponse;
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
        this.promptBuilderService =
                promptBuilderService;
    }

    private WebClient webClient() {

        return WebClient.builder()
                .baseUrl(
                        "https://generativelanguage.googleapis.com"
                )
                .build();
    }

    public AiAnalysisResponse analyzeSkillDelta(
            QuestionAttempt attempt
    ) {

        String prompt =
                promptBuilderService
                        .buildSkillDeltaPrompt(
                                attempt
                        );

        String geminiText =
                askGemini(prompt);

        try {

            return objectMapper.readValue(
                    geminiText,
                    AiAnalysisResponse.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse Gemini skill response",
                    e
            );
        }
    }

    public String generateDetailedInsights(
            QuestionAttempt attempt
    ) {

        String prompt =
                promptBuilderService
                        .buildDetailedInsightsPrompt(
                                attempt
                        );

        return askGemini(prompt);
    }

    public String askGemini(
            String prompt
    ) {

        Map<String, Object> body =
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

        if (
                response == null
                        || response.isBlank()
        ) {

            throw new RuntimeException(
                    "Empty response from Gemini"
            );
        }

        try {

            JsonNode root =
                    objectMapper.readTree(
                            response
                    );

            JsonNode candidates =
                    root.path("candidates");

            if (
                    !candidates.isArray()
                            || candidates.isEmpty()
            ) {

                throw new RuntimeException(
                        "Gemini returned no candidates"
                );
            }

            JsonNode parts =
                    candidates
                            .get(0)
                            .path("content")
                            .path("parts");

            if (
                    !parts.isArray()
                            || parts.isEmpty()
            ) {

                throw new RuntimeException(
                        "Gemini returned no content"
                );
            }

            String geminiText =
                    parts
                            .get(0)
                            .path("text")
                            .asText();

            if (
                    geminiText == null
                            || geminiText.isBlank()
            ) {

                throw new RuntimeException(
                        "Gemini returned empty text"
                );
            }

            return geminiText
                    .replace(
                            "```json",
                            ""
                    )
                    .replace(
                            "```",
                            ""
                    )
                    .trim();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse Gemini response",
                    e
            );
        }
    }
}