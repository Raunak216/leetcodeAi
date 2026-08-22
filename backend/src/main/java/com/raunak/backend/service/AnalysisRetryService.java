package com.raunak.backend.service;

import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.repository.QuestionAttemptRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisRetryService {

    private final QuestionAttemptRepository repository;
    private final QuestionAttemptService questionAttemptService;

    public AnalysisRetryService(
            QuestionAttemptRepository repository,
            QuestionAttemptService questionAttemptService
    ) {
        this.repository =
                repository;

        this.questionAttemptService =
                questionAttemptService;
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

        for (QuestionAttempt attempt :
                attempts) {

            questionAttemptService.retryAnalysis(
                    attempt
            );
        }
    }
}