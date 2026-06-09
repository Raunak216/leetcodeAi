package com.raunak.backend.service;

import com.raunak.backend.dto.QuestionAttemptRequest;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.QuestionAttemptRepository;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionAttemptService {

    private final QuestionAttemptRepository questionAttemptRepository;
    private final UserRepository userRepository;

    public QuestionAttemptService(QuestionAttemptRepository questionAttemptRepository, UserRepository userRepository) {
        this.questionAttemptRepository = questionAttemptRepository;
        this.userRepository = userRepository;
    }

    public QuestionAttempt saveAttempt(QuestionAttemptRequest request) {

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        QuestionAttempt attempt = new QuestionAttempt();
        attempt.setQuestionSlug(request.getQuestionSlug());
        attempt.setTitle(request.getTitle());
        attempt.setTopic(request.getTopic());
        attempt.setDifficulty(request.getDifficulty());
        attempt.setAttempts(request.getAttempts());
        attempt.setTimeSpent(request.getTimeSpent());
        attempt.setAccepted(request.isAccepted());
        attempt.setUser(user);

        return questionAttemptRepository.save(attempt);
    }

    public List<QuestionAttempt> getAttemptsByUser(int userId) {
        return questionAttemptRepository.findByUserId(userId);
    }
}