package com.raunak.backend.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.DsaSkillTopics;
import com.raunak.backend.EngineeringSkillTopics;
import com.raunak.backend.ReasoningSkillTopics;
import com.raunak.backend.dto.SkillValue;
import com.raunak.backend.enums.SkillSignal;
import com.raunak.backend.model.SkillProfile;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.SkillProfileRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
    public class SkillProfileService {

        private final SkillProfileRepository skillProfileRepository;
        private final ObjectMapper objectMapper;

        public SkillProfileService(
                SkillProfileRepository skillProfileRepository,
                ObjectMapper objectMapper
        )
        {
            this.skillProfileRepository =
                    skillProfileRepository;

            this.objectMapper =
                    objectMapper;
        }
    private String createInitialDsa() {

        try {

            Map<String, SkillValue> skills =
                    new HashMap<>();

            for (
                    String topic :
                    DsaSkillTopics.ALL_TOPICS
            ) {

                skills.put(
                        topic,
                        new SkillValue(
                                null,
                                0
                        )
                );
            }

            return objectMapper
                    .writeValueAsString(
                            skills
                    );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
    private String createInitialEngineering() {

        try {

            Map<String, SkillValue> skills =
                    new HashMap<>();

            for (
                    String topic :
                    EngineeringSkillTopics.ALL_TOPICS
            ) {

                skills.put(
                        topic,
                        new SkillValue(
                                null,
                                0
                        )
                );
            }

            return objectMapper
                    .writeValueAsString(
                            skills
                    );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
    private String createInitialReasoning() {

        try {

            Map<String, SkillValue> skills =
                    new HashMap<>();

            for (
                    String topic :
                    ReasoningSkillTopics.ALL_TOPICS
            ) {

                skills.put(
                        topic,
                        new SkillValue(
                                null,
                                0
                        )
                );
            }

            return objectMapper
                    .writeValueAsString(
                            skills
                    );

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
        private double getSignalValue(
                SkillSignal signal
        ) {

            return switch (signal) {

                case EFFICIENT_SOLVE -> 100;

                case CLEAN_SOLVE -> 85;

                case STRUGGLE -> 50;

                case MISTAKE -> 20;
            };
        }

        private double updateMastery(
                Double mastery,
                int attempts,
                double signal
        ) {

            if (mastery == null) {
                return signal;
            }

            double alpha =
                    Math.max(
                            1.0 / (attempts + 1),
                            0.2
                    );

            return mastery +
                    alpha *
                            (signal - mastery);
        }

    private String getJsonForTopic(
            SkillProfile profile,
            String topic
    ) {

        if (
                DsaSkillTopics.contains(
                        topic
                )
        ) {
            return profile.getDsa();
        }

        if (
                EngineeringSkillTopics.contains(
                        topic
                )
        ) {
            return profile.getEngineering();
        }

        return profile.getReasoning();
    }

    private void setJsonForTopic(
            SkillProfile profile,
            String topic,
            String json
    ) {

        if (
                DsaSkillTopics.contains(
                        topic
                )
        ) {

            profile.setDsa(
                    json
            );

            return;
        }

        if (
                EngineeringSkillTopics.contains(
                        topic
                )
        ) {

            profile.setEngineering(
                    json
            );

            return;
        }

        profile.setReasoning(
                json
        );
    }

        public void applySignal(
                int userId,
                String topic,
                SkillSignal signal
        ) {

            try {

                SkillProfile profile =
                        skillProfileRepository
                                .findByUserId(userId)
                                .orElseThrow();

                Map<String, SkillValue> current =
                        objectMapper.readValue(
                                getJsonForTopic(
                                        profile,
                                        topic
                                ),
                                new TypeReference<
                                        Map<String, SkillValue>
                                        >() {}
                        );

                SkillValue skillValue =
                        current.get(topic);

                if (skillValue == null) {

                    skillValue =
                            new SkillValue(
                                    null,
                                    0
                            );
                }

                double signalValue =
                        getSignalValue(signal);

                Double mastery =
                        updateMastery(
                                skillValue.getMastery(),
                                skillValue.getAttempts(),
                                signalValue
                        );

                skillValue.setMastery(
                        mastery
                );

                skillValue.setAttempts(
                        skillValue.getAttempts() + 1
                );

                current.put(
                        topic,
                        skillValue
                );

                String updatedJson =
                        objectMapper.writeValueAsString(
                                current
                        );

                setJsonForTopic(
                        profile,
                        topic,
                        updatedJson
                );

                skillProfileRepository.save(
                        profile
                );

            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }

    public SkillProfile getOrCreateProfile(
            User user
    ) {

        return skillProfileRepository
                .findByUserId(
                        user.getId()
                )
                .orElseGet(() -> {

                    SkillProfile profile =
                            new SkillProfile();

                    profile.setUser(
                            user
                    );

                    profile.setDsa(
                            createInitialDsa()
                    );

                    profile.setEngineering(
                            createInitialEngineering()
                    );

                    profile.setReasoning(
                            createInitialReasoning()
                    );

                    return skillProfileRepository
                            .save(profile);
                });
    }
    public boolean isValidTopic(
            String topic
    ) {

        return
                DsaSkillTopics.contains(topic)

                        ||

                        EngineeringSkillTopics.contains(topic)

                        ||

                        ReasoningSkillTopics.contains(topic);
    }
}
