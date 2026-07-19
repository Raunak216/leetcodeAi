package com.raunak.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.AnalysisResult;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.repository.QuestionAttemptRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisRetryService {

    private final QuestionAttemptRepository repository;

    private final GeminiService geminiService;

    private final AnalysisMapperService analysisMapperService;

    private final ObjectMapper objectMapper;

    public AnalysisRetryService(
            QuestionAttemptRepository repository,
            GeminiService geminiService,
            AnalysisMapperService analysisMapperService,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.geminiService = geminiService;
        this.analysisMapperService = analysisMapperService;
        this.objectMapper = objectMapper;
    }

    @Scheduled(
            fixedDelay = 300000
    )
    public void retryFailedAnalysis() {

        List<QuestionAttempt> attempts =
                repository
                        .findByAnalysisCompletedFalseAndAnalysisRetryCountLessThan(
                                10
                        );

        for (
                QuestionAttempt attempt :
                attempts
        ) {

            try {

                AnalysisResult result =
                        geminiService.analyze(
                                attempt
                        );

                analysisMapperService.applyAnalysis(
                        attempt.getUser().getId(),
                        result
                );

                attempt.setAiResponseJson(
                        objectMapper.writeValueAsString(
                                result
                        )
                );

                attempt.setAnalysisCompleted(
                        true
                );

                repository.save(
                        attempt
                );

            } catch (Exception e) {

                attempt.setAnalysisRetryCount(
                        attempt.getAnalysisRetryCount() + 1
                );

                repository.save(
                        attempt
                );
            }
        }
    }
}