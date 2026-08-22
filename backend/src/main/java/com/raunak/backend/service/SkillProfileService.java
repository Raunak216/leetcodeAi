package com.raunak.backend.service;

import com.raunak.backend.DsaSkillTopics;
import com.raunak.backend.ProblemSolvingSignals;
import com.raunak.backend.dto.AiAnalysisResponse;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.User;
import com.raunak.backend.model.UserSkill;
import com.raunak.backend.repository.UserSkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SkillProfileService {

    private static final double DEFAULT_RATING = 50.0;

    private static final double DECAY_FLOOR = 30.0;

    private static final double HALF_LIFE_DAYS = 14.0;

    private static final double ELO_K = 8.0;

    private static final double DIFFICULTY_SCALE = 40.0;

    private static final double MAX_PENALTY = 0.50;

    private final UserSkillRepository userSkillRepository;

    public SkillProfileService(
            UserSkillRepository userSkillRepository
    ) {
        this.userSkillRepository =
                userSkillRepository;
    }

    public List<UserSkill> getSkills(int userId) {

        List<UserSkill> skills =
                userSkillRepository.findByUserId(userId);

        Set<String> existingSkills =
                new HashSet<>();

        for (UserSkill skill : skills) {
            existingSkills.add(skill.getSkill());
        }

        List<UserSkill> newSkills =
                new ArrayList<>();

        for (String skill :
                DsaSkillTopics.ALL_TOPICS) {

            if (!existingSkills.contains(skill)) {

                newSkills.add(
                        createSkill(skill)
                );
            }
        }

        for (String skill :
                ProblemSolvingSignals.ALL_SIGNALS) {

            if (!existingSkills.contains(skill)) {

                newSkills.add(
                        createSkill(skill)
                );
            }
        }

        if (!newSkills.isEmpty()) {

            for (UserSkill skill : newSkills) {

                User user = new User();
                user.setId(userId);

                skill.setUser(user);
            }

            userSkillRepository.saveAll(
                    newSkills
            );

            skills.addAll(newSkills);
        }

        skills.sort(
                (a, b) ->
                        a.getSkill()
                                .compareTo(
                                        b.getSkill()
                                )
        );

        return skills;
    }

    private UserSkill createSkill(
            String skillName
    ) {

        UserSkill skill =
                new UserSkill();

        skill.setSkill(
                skillName
        );

        skill.setRating(
                DEFAULT_RATING
        );

        skill.setAttempts(
                0
        );

        skill.setLastPracticedAt(
                null
        );

        return skill;
    }

    @Transactional
    public void applyAnalysis(
            QuestionAttempt attempt,
            AiAnalysisResponse analysis
    ) {

        if (analysis == null) {
            throw new RuntimeException(
                    "Empty AI analysis"
            );
        }

        if (analysis.getTags() == null) {
            return;
        }

        double performance =
                calculatePerformance(
                        attempt,
                        analysis.getScore()
                );

        List<String> processedTags =
                new ArrayList<>();

        int tagCount = 0;

        for (String rawTag :
                analysis.getTags()) {

            if (tagCount >= 3) {
                break;
            }

            if (rawTag == null) {
                continue;
            }

            String tag =
                    rawTag
                            .trim()
                            .toLowerCase();

            if (!isValidSkill(tag)) {
                continue;
            }

            if (processedTags.contains(tag)) {
                continue;
            }

            updateSkill(
                    attempt,
                    tag,
                    performance
            );

            processedTags.add(tag);

            tagCount++;
        }
    }

    private void updateSkill(
            QuestionAttempt attempt,
            String skillName,
            double performance
    ) {

        int userId =
                attempt
                        .getUser()
                        .getId();

        UserSkill skill =
                userSkillRepository
                        .findByUserIdAndSkill(
                                userId,
                                skillName
                        )
                        .orElseGet(() -> {

                            UserSkill newSkill =
                                    createSkill(
                                            skillName
                                    );

                            User user =
                                    new User();

                            user.setId(
                                    userId
                            );

                            newSkill.setUser(
                                    user
                            );

                            return newSkill;
                        });

        double currentRating =
                skill.getRating();

        double decayedRating =
                calculateDecay(
                        currentRating,
                        skill.getLastPracticedAt()
                );

        double difficulty =
                getDifficultyRating(
                        attempt.getDifficulty()
                );

        double expected =
                calculateExpected(
                        difficulty,
                        decayedRating
                );

        double newRating =
                decayedRating
                        + ELO_K
                        * (performance - expected);

        newRating =
                clamp(
                        newRating,
                        0.0,
                        100.0
                );

        skill.setRating(
                newRating
        );

        skill.setAttempts(
                skill.getAttempts() + 1
        );

        skill.setLastPracticedAt(
                LocalDateTime.now()
        );

        userSkillRepository.save(
                skill
        );
    }

    private double calculatePerformance(
            QuestionAttempt attempt,
            double aiScore
    ) {

        double score =
                clamp(
                        aiScore,
                        0.0,
                        1.0
                );

        int syntaxErrors =
                attempt.getCompileErrors() == null
                        ? 0
                        : attempt.getCompileErrors();

        int logicFailures =
                attempt.getLogicFailures() == null
                        ? 0
                        : attempt.getLogicFailures();

        double penalty =
                Math.min(
                        MAX_PENALTY,
                        0.10 * logicFailures
                                + 0.02 * syntaxErrors
                );

        return Math.max(
                0.10,
                score - penalty
        );
    }

    private double calculateDecay(
            double rating,
            LocalDateTime lastPracticedAt
    ) {

        if (lastPracticedAt == null) {
            return rating;
        }

        LocalDateTime now =
                LocalDateTime.now();

        long seconds =
                Duration.between(
                        lastPracticedAt,
                        now
                ).getSeconds();

        if (seconds <= 0) {
            return rating;
        }

        double daysInactive =
                seconds / 86400.0;

        double decay =
                Math.pow(
                        2,
                        -daysInactive
                                / HALF_LIFE_DAYS
                );

        return DECAY_FLOOR
                + (rating - DECAY_FLOOR)
                * decay;
    }

    private double calculateExpected(
            double difficulty,
            double rating
    ) {

        return 1.0 /
                (
                        1.0 +
                                Math.pow(
                                        10,
                                        (difficulty - rating)
                                                / DIFFICULTY_SCALE
                                )
                );
    }

    private double getDifficultyRating(
            String difficulty
    ) {

        if (difficulty == null) {
            return 50.0;
        }

        if (difficulty.equalsIgnoreCase("Easy")) {
            return 30.0;
        }

        if (difficulty.equalsIgnoreCase("Hard")) {
            return 70.0;
        }

        return 50.0;
    }

    private boolean isValidSkill(
            String skill
    ) {

        return DsaSkillTopics.contains(skill)
                || ProblemSolvingSignals.contains(skill);
    }

    private double clamp(
            double value,
            double min,
            double max
    ) {

        if (value < min) {
            return min;
        }

        if (value > max) {
            return max;
        }

        return value;
    }
}