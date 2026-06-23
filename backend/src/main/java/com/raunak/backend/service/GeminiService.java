package com.raunak.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.config.GeminiConfig;
import com.raunak.backend.dto.AnalysisResult;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.SkillProfile;
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
                        .buildAnalysisPrompt(attempt);

        String geminiText =
                askGemini(
                        prompt
                );

        try {

            return objectMapper.readValue(
                    geminiText,
                    AnalysisResult.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    e
            );
        }
    }

    public String buildRecommendationPrompt(
            SkillProfile profile,
            List<String> solvedQuestions
    )
    {
        return """
You are an expert DSA mentor.

Analyze the user's skills and solved questions.

Recommend 10 LeetCode questions.

Rules:

1. Focus on weak skills.
2. Cover unexplored topics.
3. Avoid recommending already solved questions.
4. Balance interview preparation and learning.
5. Return ONLY valid JSON.

Return format:

{
  "recommendedQuestions":[
      "Question Name"
  ],
  "reasoning":"..."
}

User Skill Profile:
"""
                + profile.getDsa()

                + """

Engineering Profile:
"""

                + profile.getEngineering()

                + """

Reasoning Profile:
"""

                + profile.getReasoning()

                + """

Previously Solved Questions:
"""

                + solvedQuestions;
    }
    public String askGemini(
            String prompt
    ) {

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
                    e
            );
        }
    }
}