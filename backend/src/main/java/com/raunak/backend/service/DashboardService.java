package com.raunak.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raunak.backend.dto.DashboardResponse;
import com.raunak.backend.dto.SkillValue;
import com.raunak.backend.dto.TopicMastery;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.SkillProfile;
import com.raunak.backend.model.User;
import com.raunak.backend.repository.QuestionAttemptRepository;
import com.raunak.backend.repository.SkillProfileRepository;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final QuestionAttemptRepository repository;
    private final UserRepository userRepository;
    private final SkillProfileRepository skillProfileRepository;
    private final ObjectMapper objectMapper;

    public DashboardService(
            QuestionAttemptRepository repository,
            UserRepository userRepository,
            SkillProfileRepository skillProfileRepository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.skillProfileRepository = skillProfileRepository;
        this.objectMapper = objectMapper;
    }

    private Map<String, SkillValue> getAllSkills(SkillProfile profile) throws Exception {
        Map<String, SkillValue> all = new HashMap<>();

        all.putAll(
                objectMapper.readValue(
                        profile.getDsa(),
                        new TypeReference<Map<String, SkillValue>>() {
                        }
                )
        );

        all.putAll(
                objectMapper.readValue(
                        profile.getEngineering(),
                        new TypeReference<Map<String, SkillValue>>() {
                        }
                )
        );

        all.putAll(
                objectMapper.readValue(
                        profile.getReasoning(),
                        new TypeReference<Map<String, SkillValue>>() {
                        }
                )
        );

        return all;
    }

    private List<TopicMastery> getStrongTopics(SkillProfile profile) {
        try {
            Map<String, SkillValue> all = getAllSkills(profile);

            return all.entrySet()
                    .stream()
                    .filter(e -> e.getValue().getMastery() != null)
                    .sorted((a, b) -> Double.compare(
                            b.getValue().getMastery(),
                            a.getValue().getMastery()
                    ))
                    .map(entry -> new TopicMastery(
                            entry.getKey(),
                            entry.getValue().getMastery()
                    ))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<TopicMastery> getWeakTopics(SkillProfile profile) {
        try {
            Map<String, SkillValue> all = getAllSkills(profile);

            return all.entrySet()
                    .stream()
                    .filter(e -> e.getValue().getAttempts() > 0 && e.getValue().getMastery() != null)
                    .sorted(Comparator.comparingDouble(e -> e.getValue().getMastery()))
                    // .limit(5) removed to fetch all skills for /skills page
                    .map(e -> new TopicMastery(
                            e.getKey(),
                            e.getValue().getMastery()
                    ))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getUnexploredTopics(SkillProfile profile) {
        try {
            Map<String, SkillValue> all = getAllSkills(profile);

            return all.entrySet()
                    .stream()
                    .filter(e -> e.getValue().getAttempts() == 0)
                    .map(Map.Entry::getKey)
                    .sorted()
                    // .limit(5) removed to fetch all skills for /skills page
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DashboardResponse getDashboard(int userId) {
        User user = userRepository.findById(userId).orElseThrow();
        SkillProfile profile = skillProfileRepository.findByUserId(userId).orElseThrow();
        List<QuestionAttempt> attempts = repository.findByUserId(userId);

        DashboardResponse response = new DashboardResponse();

        response.setTotalAttempts(attempts.size());

        response.setQuestionsSolved(
                (int) attempts.stream()
                        .map(QuestionAttempt::getQuestionSlug)
                        .distinct()
                        .count()
        );

        response.setAnalyzedAttempts(
                (int) attempts.stream()
                        .filter(a -> Boolean.TRUE.equals(a.getAnalysisCompleted()))
                        .count()
        );

        response.setUserName(user.getUserName());
        response.setLeetcodeVerified(user.isLeetcodeVerified());

        response.setStrongTopics(getStrongTopics(profile));
        response.setWeakTopics(getWeakTopics(profile));
        response.setUnexploredTopics(getUnexploredTopics(profile));

        return response;
    }
}