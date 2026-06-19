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

    public QuestionAttemptService(
            QuestionAttemptRepository questionAttemptRepository,
            UserRepository userRepository
    ) {
        this.questionAttemptRepository =
                questionAttemptRepository;

        this.userRepository =
                userRepository;
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

        return questionAttemptRepository
                .save(attempt);
    }

    public List<QuestionAttempt> getAttemptsByUser(int userId) {
        return questionAttemptRepository.findByUserId(userId);
    }
}