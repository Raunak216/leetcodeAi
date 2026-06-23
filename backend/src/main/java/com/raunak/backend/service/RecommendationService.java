package com.raunak.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.RecommendationResponse;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.SkillProfile;
import com.raunak.backend.repository.QuestionAttemptRepository;
import com.raunak.backend.repository.SkillProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final QuestionAttemptRepository questionAttemptRepository;

    private final SkillProfileRepository skillProfileRepository;

    private final GeminiService geminiService;

    private final ObjectMapper objectMapper;

    private final PromptBuilderService promptBuilderService;

    public RecommendationService(
            QuestionAttemptRepository questionAttemptRepository,
            SkillProfileRepository skillProfileRepository,
            GeminiService geminiService,
            ObjectMapper objectMapper,
            PromptBuilderService promptBuilderService
    ) {

        this.questionAttemptRepository =
                questionAttemptRepository;

        this.skillProfileRepository =
                skillProfileRepository;

        this.geminiService =
                geminiService;

        this.objectMapper =
                objectMapper;

        this.promptBuilderService =
                promptBuilderService;
    }

    public RecommendationResponse
    getGeneralRecommendations(
            int userId
    ) {

        try {

            SkillProfile profile =
                    skillProfileRepository
                            .findByUserId(
                                    userId
                            )
                            .orElseThrow();

            List<QuestionAttempt> attempts =
                    questionAttemptRepository
                            .findByUserId(
                                    userId
                            );

            List<String> solvedQuestions =
                    attempts.stream()
                            .map(
                                    QuestionAttempt::getTitle
                            )
                            .toList();

            String prompt =
                    promptBuilderService
                            .buildRecommendationPrompt(
                                    profile,
                                    solvedQuestions
                            );
            System.out.println(prompt);
            String geminiResponse =
                    geminiService
                            .askGemini(
                                    prompt
                            );

            return objectMapper
                    .readValue(
                            geminiResponse,
                            RecommendationResponse.class
                    );

        } catch (Exception e) {

            throw new RuntimeException(
                    e
            );
        }
    }
}