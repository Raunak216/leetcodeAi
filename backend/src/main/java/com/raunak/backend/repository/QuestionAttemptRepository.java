package com.raunak.backend.repository;

import com.raunak.backend.model.QuestionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionAttemptRepository
        extends JpaRepository<QuestionAttempt, Integer> {

    List<QuestionAttempt> findByUserIdOrderByCreatedAtDesc(
            int userId
    );

    List<QuestionAttempt>
    findByAnalysisCompletedFalseAndAnalysisRetryCountLessThan(
            int maxRetry
    );
}