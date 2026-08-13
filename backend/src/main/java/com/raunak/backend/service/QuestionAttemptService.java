package com.raunak.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.AnalysisResult;
import com.raunak.backend.dto.QuestionAttemptRequest;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.SkillProfile;
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
    private final AiAnalysisService aiAnalysisService;
    private final GeminiService geminiService;
    private final AnalysisMapperService analysisMapperService;
    private final ObjectMapper objectMapper;
    private final SkillProfileService skillProfileService;

    public QuestionAttemptService(
            QuestionAttemptRepository questionAttemptRepository,
            UserRepository userRepository, AiAnalysisService aiAnalysisService, GeminiService geminiService,
            AnalysisMapperService analysisMapperService, ObjectMapper objectMapper,
            SkillProfileService skillProfileService) {
        this.questionAttemptRepository = questionAttemptRepository;

        this.userRepository = userRepository;
        this.aiAnalysisService = aiAnalysisService;
        this.geminiService = geminiService;
        this.analysisMapperService = analysisMapperService;
        this.objectMapper = objectMapper;
        this.skillProfileService = skillProfileService;
    }

    public QuestionAttempt saveAttempt(
            QuestionAttemptRequest request) {

        AuthUser authUser =
                (AuthUser) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        int userId = authUser.getUserId();

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        SkillProfile profile = skillProfileService.getOrCreateProfile(user);

        QuestionAttempt attempt = new QuestionAttempt();
        attempt.setQuestionSlug(request.getQuestionSlug());
        attempt.setTitle(request.getTitle());
        attempt.setDifficulty(request.getDifficulty());
        attempt.setLanguage(request.getLanguage());
        attempt.setRuntime(request.getRuntime());
        attempt.setMemory(request.getMemory());
        attempt.setJourneyJson(request.getJourneyJson());
        attempt.setCreatedAt(LocalDateTime.now());
        attempt.setUser(user);

        QuestionAttempt saved = questionAttemptRepository.save(attempt);
        try {
            AnalysisResult result = geminiService.analyze(saved);
            analysisMapperService.applyAnalysis(saved.getUser().getId(), result);
            saved.setAiResponseJson(objectMapper.writeValueAsString(result));

            saved.setAnalysisCompleted(true);

            questionAttemptRepository.save(saved);

        } catch (Exception e) {

            e.printStackTrace();
            saved.setAnalysisCompleted(false);
            saved.setAnalysisRetryCount(saved.getAnalysisRetryCount() + 1);
            questionAttemptRepository.save(saved);
        }

        return saved;
    }

    public List<QuestionAttempt> getAttemptsByUser(int userId) {
        return questionAttemptRepository.findByUserId(userId);
    }

    public QuestionAttempt getAttempt(
            int attemptId,
            int userId
    ) {
        QuestionAttempt attempt =
                questionAttemptRepository.findById(attemptId).orElseThrow();

        if (attempt.getUser().getId() != userId) {
            throw new RuntimeException("Forbidden");
        }

        return attempt;
    }
}