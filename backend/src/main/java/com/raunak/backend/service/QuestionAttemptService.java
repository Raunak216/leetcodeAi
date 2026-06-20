package com.raunak.backend.service;

import com.raunak.backend.dto.QuestionAttemptRequest;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.QuestionAttemptRepository;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionAttemptService {

    private final QuestionAttemptRepository questionAttemptRepository;
    private final UserRepository userRepository;
    private final AiAnalysisService aiAnalysisService;

    public QuestionAttemptService(
            QuestionAttemptRepository questionAttemptRepository,
            UserRepository userRepository, AiAnalysisService aiAnalysisService
    ) {
        this.questionAttemptRepository =
                questionAttemptRepository;

        this.userRepository =
                userRepository;
        this.aiAnalysisService =
                aiAnalysisService;
    }

    public QuestionAttempt saveAttempt(
            QuestionAttemptRequest request
    ) {

        User user =
                userRepository
                        .findById(
                                request.getUserId()
                        )
                        .orElseThrow(
                                () ->
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
        attempt.setUser(
                user
        );

        QuestionAttempt saved = questionAttemptRepository.save(attempt);
        aiAnalysisService.analyze(saved);
        return saved;
    }


    public List<QuestionAttempt> getAttemptsByUser(int userId) {
        return questionAttemptRepository.findByUserId(userId);
    }
}