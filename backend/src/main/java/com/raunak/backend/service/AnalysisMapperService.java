package com.raunak.backend.service;

import com.raunak.backend.dto.AnalysisResult;
import com.raunak.backend.enums.SkillSignal;
import org.springframework.stereotype.Service;

@Service
public class AnalysisMapperService {

    private final SkillProfileService skillProfileService;

    public AnalysisMapperService(
            SkillProfileService skillProfileService
    ) {
        this.skillProfileService = skillProfileService;
    }

    public void applyAnalysis(
            int userId,
            AnalysisResult result
    ) {

        if (
                result.getDsaSignals() != null
        ) {

            result.getDsaSignals()
                    .forEach((topic, signal) -> {

                        skillProfileService.applySignal(
                                userId,
                                topic,
                                SkillSignal.valueOf(signal)
                        );
                    });
        }

        if (
                result.getEngineeringSignals() != null
        ) {

            result.getEngineeringSignals()
                    .forEach((topic, signal) -> {

                        skillProfileService.applySignal(
                                userId,
                                topic,
                                SkillSignal.valueOf(signal)
                        );
                    });
        }

        if (
                result.getReasoningSignals() != null
        ) {

            result.getReasoningSignals()
                    .forEach((topic, signal) -> {

                        skillProfileService.applySignal(
                                userId,
                                topic,
                                SkillSignal.valueOf(signal)
                        );
                    });
        }
    }
}