package com.raunak.backend.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.SkillTopics;
import com.raunak.backend.model.Skills;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.SkillsRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
    @Service
    public class SkillsService {

        private final SkillsRepository skillsRepository;

        private final ObjectMapper objectMapper;

        public SkillsService(
                SkillsRepository skillsRepository,
                ObjectMapper objectMapper
        ) {
            this.skillsRepository =
                    skillsRepository;

            this.objectMapper =
                    objectMapper;
        }
        private String createInitialSkills() {

            try {

                Map<String,Integer> skills =
                        new HashMap<>();

                for(String topic : SkillTopics.ALL_TOPICS){

                    skills.put(
                            topic,
                            50
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

        public void applyDelta(
                int userId,
                Map<String,Integer> delta
        ) {

            try {

                Skills skills =
                        skillsRepository
                                .findByUserId(
                                        userId
                                )
                                .orElseThrow();

                Map<String,Integer> current =
                        objectMapper.readValue(
                                skills.getSkillsJson(),
                                new TypeReference<>() {}
                        );

                for (
                        String topic :
                        delta.keySet()
                ) {

                    int currentScore =
                            current.getOrDefault(
                                    topic,
                                    -1
                            );

                    if (currentScore == -1) {
                        currentScore = 50;
                    }
                    int newScore =
                            currentScore +
                                    delta.get(topic);

                    newScore =
                            Math.max(
                                    0,
                                    Math.min(
                                            100,
                                            newScore
                                    )
                            );

                    current.put(
                            topic,
                            newScore
                    );
                }

                skills.setSkillsJson(
                        objectMapper.writeValueAsString(
                                current
                        )
                );

                skillsRepository.save(
                        skills
                );

            } catch (Exception e) {

                throw new RuntimeException(
                        e
                );
            }
        }
        public Skills getOrCreateSkills(
                User user
        ) {

            return skillsRepository
                    .findByUserId(
                            user.getId()
                    )
                    .orElseGet(() -> {

                        Skills skills =
                                new Skills();

                        skills.setUser(
                                user
                        );

                        skills.setSkillsJson(
                                createInitialSkills()
                        );

                        return skillsRepository
                                .save(skills);
                    });
        }
}
