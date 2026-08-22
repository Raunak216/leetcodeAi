package com.raunak.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.CompanyRecommendationRequest;
import com.raunak.backend.dto.GeneralRecommendationRequest;
import com.raunak.backend.dto.RecommendationResponse;
import com.raunak.backend.model.Company;
import com.raunak.backend.model.CompanyQuestion;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.UserSkill;
import com.raunak.backend.repository.CompanyQuestionRepository;
import com.raunak.backend.repository.CompanyRepository;
import com.raunak.backend.repository.QuestionAttemptRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RecommendationService {

    private final QuestionAttemptRepository questionAttemptRepository;
    private final SkillProfileService skillProfileService;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;
    private final PromptBuilderService promptBuilderService;
    private final CompanyRepository companyRepository;
    private final CompanyQuestionRepository companyQuestionRepository;

    public RecommendationService(
            QuestionAttemptRepository questionAttemptRepository,
            SkillProfileService skillProfileService,
            GeminiService geminiService,
            ObjectMapper objectMapper,
            PromptBuilderService promptBuilderService,
            CompanyRepository companyRepository,
            CompanyQuestionRepository companyQuestionRepository
    ) {
        this.questionAttemptRepository =
                questionAttemptRepository;

        this.skillProfileService =
                skillProfileService;

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

    public RecommendationResponse getGeneralRecommendations(
            int userId,
            GeneralRecommendationRequest request
    ) {

        try {

            List<UserSkill> skills =
                    skillProfileService.getSkills(
                            userId
                    );

            List<QuestionAttempt> attempts =
                    questionAttemptRepository
                            .findByUserIdOrderByCreatedAtDesc(
                                    userId
                            );

            List<String> solvedQuestions =
                    getSolvedQuestions(
                            attempts
                    );

            String prompt =
                    promptBuilderService
                            .buildRecommendationPrompt(
                                    skills,
                                    solvedQuestions,
                                    request.isInterviewScheduled(),
                                    request.getDaysRemaining()
                            );

            String response =
                    geminiService.askGemini(
                            prompt
                    );

            return objectMapper.readValue(
                    response,
                    RecommendationResponse.class
            );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public RecommendationResponse getCompanyRecommendations(
            int userId,
            CompanyRecommendationRequest request
    ) {

        try {

            List<UserSkill> skills =
                    skillProfileService.getSkills(
                            userId
                    );

            List<QuestionAttempt> attempts =
                    questionAttemptRepository
                            .findByUserIdOrderByCreatedAtDesc(
                                    userId
                            );

            Company company =
                    companyRepository
                            .findByName(
                                    request.getCompany()
                            )
                            .orElseThrow();

            List<String> companyQuestions =
                    new ArrayList<>(
                            companyQuestionRepository
                                    .findByCompanyId(
                                            company.getId()
                                    )
                                    .stream()
                                    .map(
                                            CompanyQuestion::getTitle
                                    )
                                    .distinct()
                                    .toList()
                    );

            List<String> solvedQuestions =
                    getSolvedQuestions(
                            attempts
                    );

            List<String> solvedCompanyQuestions =
                    new ArrayList<>();

            for (String question :
                    solvedQuestions) {

                if (
                        companyQuestions.contains(
                                question
                        )
                ) {

                    solvedCompanyQuestions.add(
                            question
                    );
                }
            }

            Collections.shuffle(
                    companyQuestions
            );

            if (
                    companyQuestions.size() > 200
            ) {

                companyQuestions =
                        companyQuestions.subList(
                                0,
                                200
                        );
            }

            String prompt =
                    promptBuilderService
                            .buildCompanyRecommendationPrompt(
                                    skills,
                                    solvedCompanyQuestions,
                                    companyQuestions,
                                    request.getCompany(),
                                    request.getDaysRemaining()
                            );

            String response =
                    geminiService.askGemini(
                            prompt
                    );

            return objectMapper.readValue(
                    response,
                    RecommendationResponse.class
            );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    private List<String> getSolvedQuestions(
            List<QuestionAttempt> attempts
    ) {

        List<String> solvedQuestions =
                new ArrayList<>();

        for (QuestionAttempt attempt :
                attempts) {

            if (
                    !solvedQuestions.contains(
                            attempt.getTitle()
                    )
            ) {

                solvedQuestions.add(
                        attempt.getTitle()
                );
            }
        }

        return solvedQuestions;
    }
}