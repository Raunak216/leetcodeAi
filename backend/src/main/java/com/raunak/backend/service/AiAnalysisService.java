package com.raunak.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.AnalysisResult;
import com.raunak.backend.enums.SkillSignal;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.repository.QuestionAttemptRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiAnalysisService {

    private final SkillsService skillsService;
    private final QuestionAttemptRepository questionAttemptRepository;
    private final AnalysisMapperService analysisMapperService;
    private final ObjectMapper objectMapper;
    public AiAnalysisService(
            SkillsService skillsService,
            QuestionAttemptRepository questionAttemptRepository,
            AnalysisMapperService analysisMapperService,ObjectMapper objectMapper
    ) {
        this.skillsService = skillsService;
        this.questionAttemptRepository = questionAttemptRepository;
        this.analysisMapperService = analysisMapperService;
        this.objectMapper=objectMapper;
    }

    public void analyze(
            QuestionAttempt attempt
    ) {
        AnalysisResult result =
                new AnalysisResult();
        Map<String,String> dsa =
                new HashMap<>();

        dsa.put(
                "arrays",
                "CLEAN_SOLVE"
        );

        dsa.put(
                "hashmap",
                "STRUGGLE"
        );

        result.setDsaSignals(dsa);
        Map<String,String> eng =
                new HashMap<>();

        eng.put(
                "edge_case_miss",
                "MISTAKE"
        );

        result.setEngineeringSignals(eng);
        Map<String,String> reasoning =
                new HashMap<>();

        reasoning.put(
                "incomplete_problem_understanding",
                "STRUGGLE"
        );

        result.setReasoningSignals(reasoning);
        result.setSummary(
                "Test analysis"
        );
        analysisMapperService.applyAnalysis(
                attempt.getUser().getId(),
                result
        );
        try {

            attempt.setAiResponseJson(
                    objectMapper.writeValueAsString(
                            result
                    )
            );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        questionAttemptRepository.save(
                attempt
        );

    }
}