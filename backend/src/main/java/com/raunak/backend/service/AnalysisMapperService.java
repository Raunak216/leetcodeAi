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
        this.skillProfileService =
                skillProfileService;
    }

    private void applySignals(
            int userId,
            java.util.Map<String, String> signals
    ) {

        if (
                signals == null
        ) {
            return;
        }

        signals.forEach(
                (topic, signalText) -> {

                    if (
                            !skillProfileService
                                    .isValidTopic(
                                            topic
                                    )
                    ) {

                        System.out.println(
                                "INVALID TOPIC: "
                                        + topic
                        );

                        return;
                    }

                    try {

                        SkillSignal signal =
                                SkillSignal.valueOf(
                                        signalText
                                );

                        skillProfileService
                                .applySignal(
                                        userId,
                                        topic,
                                        signal
                                );

                    } catch (Exception e) {

                        System.out.println(
                                "INVALID SIGNAL: "
                                        + signalText
                        );
                    }
                }
        );
    }

    public void applyAnalysis(
            int userId,
            AnalysisResult result
    ) {

        applySignals(
                userId,
                result.getDsaSignals()
        );

        applySignals(
                userId,
                result.getEngineeringSignals()
        );

        applySignals(
                userId,
                result.getReasoningSignals()
        );
    }
}