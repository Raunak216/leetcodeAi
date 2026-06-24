package com.raunak.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.RecommendationResponse;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.SkillProfile;
import com.raunak.backend.repository.QuestionAttemptRepository;
import com.raunak.backend.repository.SkillProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import com.raunak.backend.dto.CompanyRecommendationRequest;
import com.raunak.backend.model.Company;
import com.raunak.backend.model.CompanyQuestion;
import com.raunak.backend.repository.CompanyQuestionRepository;
import com.raunak.backend.repository.CompanyRepository;
@Service
public class RecommendationService {

    private final QuestionAttemptRepository questionAttemptRepository;
    private final SkillProfileRepository skillProfileRepository;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;
    private final PromptBuilderService promptBuilderService;
    private final CompanyRepository companyRepository;
    private final CompanyQuestionRepository companyQuestionRepository;

    public RecommendationService(
            QuestionAttemptRepository questionAttemptRepository,
            SkillProfileRepository skillProfileRepository,
            GeminiService geminiService,
            ObjectMapper objectMapper,
            PromptBuilderService promptBuilderService,
            CompanyRepository companyRepository,
            CompanyQuestionRepository companyQuestionRepository
    )
    {

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

        this.companyRepository =
                companyRepository;

        this.companyQuestionRepository =
                companyQuestionRepository;
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

    public RecommendationResponse
    getCompanyRecommendations(
            CompanyRecommendationRequest request
    )
    {

        try {

            SkillProfile profile =
                    skillProfileRepository
                            .findByUserId(
                                    request.getUserId()
                            )
                            .orElseThrow();

            List<String> solvedQuestions =
                    questionAttemptRepository
                            .findByUserId(
                                    request.getUserId()
                            )
                            .stream()
                            .map(
                                    QuestionAttempt::getTitle
                            )
                            .toList();

            Company company =
                    companyRepository
                            .findByName(
                                    request.getCompany()
                            )
                            .orElseThrow();

            List<String> companyQuestions =
                    companyQuestionRepository
                            .findByCompanyId(
                                    company.getId()
                            )
                            .stream()
                            .map(
                                    CompanyQuestion::getTitle
                            )
                            .distinct()
                            .toList();
            if (
                    companyQuestions.size() > 200
            ) {

                Collections.shuffle(
                        companyQuestions
                );

                companyQuestions =
                        companyQuestions.subList(
                                0,
                                200
                        );
            }
            companyQuestions =
                    companyQuestions.subList(
                            0,
                            Math.min(
                                    200,
                                    companyQuestions.size()
                            )
                    );

            String prompt =
                    promptBuilderService
                            .buildCompanyRecommendationPrompt(
                                    profile,
                                    solvedQuestions,
                                    companyQuestions,
                                    request.getCompany(),
                                    request.getDaysRemaining()
                            );

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