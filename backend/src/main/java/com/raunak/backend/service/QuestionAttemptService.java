package com.raunak.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.AnalysisResult;
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
    private final GeminiService geminiService;
    private final AnalysisMapperService analysisMapperService;
    private final ObjectMapper objectMapper;
    public QuestionAttemptService(
            QuestionAttemptRepository questionAttemptRepository,
            UserRepository userRepository, AiAnalysisService aiAnalysisService,GeminiService geminiService
            ,AnalysisMapperService analysisMapperService,ObjectMapper objectMapper
    ) {
        this.questionAttemptRepository =
                questionAttemptRepository;

        this.userRepository =
                userRepository;
        this.aiAnalysisService =
                aiAnalysisService;
        this.geminiService=geminiService;
        this.analysisMapperService=analysisMapperService;
        this.objectMapper=objectMapper;
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

        QuestionAttempt saved =
                questionAttemptRepository.save(
                        attempt
                );

        AnalysisResult result =
                geminiService.analyze(
                        saved
                );

        analysisMapperService.applyAnalysis(
                saved.getUser().getId(),
                result
        );
        try{
        saved.setAiResponseJson(
                objectMapper.writeValueAsString(
                        result
                )
        );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        questionAttemptRepository.save(
                saved
        );

        return saved;
    }


    public List<QuestionAttempt> getAttemptsByUser(int userId) {
        return questionAttemptRepository.findByUserId(userId);
    }
}