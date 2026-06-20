package com.raunak.backend.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.DsaSkillTopics;
import com.raunak.backend.dto.SkillValue;
import com.raunak.backend.enums.SkillSignal;
import com.raunak.backend.model.DsaSkillProfile;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.DsaSkillProfileRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
    @Service
    public class SkillsService {

        private final DsaSkillProfileRepository dsaSkillProfileRepository;
        private final ObjectMapper objectMapper;

        public SkillsService(
                DsaSkillProfileRepository dsaSkillProfileRepository,
                ObjectMapper objectMapper
        )
        {
            this.dsaSkillProfileRepository =
                    dsaSkillProfileRepository;

            this.objectMapper =
                    objectMapper;
        }
        private String createInitialSkills() {

            try {

                Map<String, SkillValue> skills =
                        new HashMap<>();

                for(String topic : DsaSkillTopics.ALL_TOPICS){

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

                throw new RuntimeException(
                        e
                );
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

        public void applySignal(
                int userId,
                String topic,
                SkillSignal signal
        ) {

            try {

                DsaSkillProfile profile =
                        dsaSkillProfileRepository
                                .findByUserId(userId)
                                .orElseThrow();

                Map<String, SkillValue> current =
                        objectMapper.readValue(
                                profile.getSkillsJson(),
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

                profile.setSkillsJson(
                        objectMapper.writeValueAsString(
                                current
                        )
                );

                dsaSkillProfileRepository
                        .save(profile);

            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }

        public DsaSkillProfile getOrCreateSkills(
                User user
        ) {

            return dsaSkillProfileRepository
                    .findByUserId(
                            user.getId()
                    )
                    .orElseGet(() -> {

                        DsaSkillProfile dsaSkillProfile =
                                new DsaSkillProfile();

                        dsaSkillProfile.setUser(
                                user
                        );

                        dsaSkillProfile.setSkillsJson(
                                createInitialSkills()
                        );

                        return dsaSkillProfileRepository
                                .save(dsaSkillProfile);
                    });
        }
}
