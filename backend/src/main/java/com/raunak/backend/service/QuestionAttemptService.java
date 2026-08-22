package com.raunak.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.AiAnalysisResponse;
import com.raunak.backend.dto.AiInsightsResponse;
import com.raunak.backend.dto.QuestionAttemptRequest;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.QuestionAttemptRepository;
import com.raunak.backend.repository.UserRepository;
import com.raunak.backend.security.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionAttemptService {

    private final QuestionAttemptRepository questionAttemptRepository;
    private final UserRepository userRepository;
    private final GeminiService geminiService;
    private final SkillProfileService skillProfileService;
    private final ObjectMapper objectMapper;

    public QuestionAttemptService(
            QuestionAttemptRepository questionAttemptRepository,
            UserRepository userRepository,
            GeminiService geminiService,
            SkillProfileService skillProfileService,
            ObjectMapper objectMapper
    ) {
        this.questionAttemptRepository =
                questionAttemptRepository;

        this.userRepository =
                userRepository;

        this.geminiService =
                geminiService;

        this.skillProfileService =
                skillProfileService;

        this.objectMapper =
                objectMapper;
    }

    public QuestionAttempt saveAttempt(
            QuestionAttemptRequest request
    ) {

        AuthUser authUser =
                (AuthUser)
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

        int userId =
                authUser.getUserId();

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        QuestionAttempt attempt =
                new QuestionAttempt();

        attempt.setQuestionSlug(
                request.getQuestionSlug()
        );

        attempt.setTitle(
                request.getTitle()
        );

        attempt.setDifficulty(
                request.getDifficulty()
        );

        attempt.setLanguage(
                request.getLanguage()
        );

        attempt.setRuntime(
                request.getRuntime()
        );

        attempt.setMemory(
                request.getMemory()
        );

        attempt.setJourneyJson(
                request.getJourneyJson()
        );

        attempt.setCreatedAt(
                LocalDateTime.now()
        );

        attempt.setUser(user);

        extractTelemetry(attempt);

        QuestionAttempt saved =
                questionAttemptRepository
                        .save(attempt);

        try {

            analyzeAttempt(saved);

        } catch (Exception e) {

            markAnalysisFailed(saved);
        }

        return saved;
    }

    private void analyzeAttempt(
            QuestionAttempt attempt
    ) {

        if (Boolean.TRUE.equals(
                attempt.getAnalysisCompleted()
        )) {
            return;
        }

        AiAnalysisResponse analysis =
                geminiService
                        .analyzeSkillDelta(
                                attempt
                        );

        skillProfileService.applyAnalysis(
                attempt,
                analysis
        );

        attempt.setAnalysisCompleted(
                true
        );

        questionAttemptRepository.save(
                attempt
        );
    }

    private void markAnalysisFailed(
            QuestionAttempt attempt
    ) {

        int retryCount =
                attempt.getAnalysisRetryCount() == null
                        ? 0
                        : attempt.getAnalysisRetryCount();

        attempt.setAnalysisCompleted(
                false
        );

        attempt.setAnalysisRetryCount(
                retryCount + 1
        );

        questionAttemptRepository.save(
                attempt
        );
    }

    public void retryAnalysis(
            QuestionAttempt attempt
    ) {

        if (Boolean.TRUE.equals(
                attempt.getAnalysisCompleted()
        )) {
            return;
        }

        try {

            analyzeAttempt(
                    attempt
            );

        } catch (Exception e) {

            markAnalysisFailed(
                    attempt
            );
        }
    }

    private void extractTelemetry(
            QuestionAttempt attempt
    ) {

        try {

            JsonNode root =
                    objectMapper.readTree(
                            attempt.getJourneyJson()
                    );

            JsonNode steps =
                    root.path("steps");

            int compileErrors = 0;
            int logicFailures = 0;
            boolean accepted = false;

            if (steps.isArray()) {

                for (JsonNode step : steps) {

                    String verdict =
                            step
                                    .path("verdict")
                                    .asText();

                    if ("Compile Error".equals(
                            verdict
                    )) {

                        compileErrors++;

                    } else if (
                            "Wrong Answer".equals(
                                    verdict
                            )
                                    ||
                                    "Time Limit Exceeded".equals(
                                            verdict
                                    )
                    ) {

                        logicFailures++;
                    }

                    if ("Accepted".equals(
                            verdict
                    )) {

                        accepted = true;
                    }
                }
            }

            attempt.setCompileErrors(
                    compileErrors
            );

            attempt.setLogicFailures(
                    logicFailures
            );

            attempt.setAccepted(
                    accepted
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Invalid journey JSON",
                    e
            );
        }
    }

    public List<QuestionAttempt> getAttemptsByUser(
            int userId
    ) {

        return questionAttemptRepository
                .findByUserIdOrderByCreatedAtDesc(
                        userId
                );
    }

    public QuestionAttempt getAttempt(
            int attemptId,
            int userId
    ) {

        QuestionAttempt attempt =
                questionAttemptRepository
                        .findById(attemptId)
                        .orElseThrow();

        if (
                attempt.getUser().getId()
                        != userId
        ) {

            throw new RuntimeException(
                    "Forbidden"
            );
        }

        return attempt;
    }

    public AiInsightsResponse generateInsights(
            int attemptId,
            int userId
    ) {

        QuestionAttempt attempt =
                getAttempt(
                        attemptId,
                        userId
                );

        String insights =
                geminiService
                        .generateDetailedInsights(
                                attempt
                        );

        return new AiInsightsResponse(
                insights
        );
    }
}