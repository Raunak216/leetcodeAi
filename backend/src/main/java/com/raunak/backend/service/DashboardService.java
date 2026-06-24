package com.raunak.backend.service;

import com.raunak.backend.dto.DashboardResponse;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.repository.QuestionAttemptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final QuestionAttemptRepository repository;

    public DashboardService(
            QuestionAttemptRepository repository
    ) {
        this.repository =
                repository;
    }

    public DashboardResponse getDashboard(
            int userId
    ) {

        List<QuestionAttempt> attempts =
                repository.findByUserId(
                        userId
                );

        DashboardResponse response =
                new DashboardResponse();

        response.setTotalAttempts(
                attempts.size()
        );

        response.setQuestionsSolved(
                (int) attempts.stream()
                        .map(
                                QuestionAttempt::getQuestionSlug
                        )
                        .distinct()
                        .count()
        );

        response.setAnalyzedAttempts(
                (int) attempts.stream()
                        .filter(
                                a ->
                                        Boolean.TRUE.equals(
                                                a.getAnalysisCompleted()
                                        )
                        )
                        .count()
        );

        response.setRecentAttempts(
                attempts.stream()
                        .sorted(
                                (a,b) ->
                                        b.getCreatedAt()
                                                .compareTo(
                                                        a.getCreatedAt()
                                                )
                        )
                        .limit(10)
                        .toList()
        );

        return response;
    }
}