package com.raunak.backend.service;

import com.raunak.backend.dto.DashboardResponse;
import com.raunak.backend.dto.TopicMastery;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.User;
import com.raunak.backend.model.UserSkill;
import com.raunak.backend.repository.QuestionAttemptRepository;
import com.raunak.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardService {

    private final QuestionAttemptRepository questionAttemptRepository;
    private final UserRepository userRepository;
    private final SkillProfileService skillProfileService;

    public DashboardService(
            QuestionAttemptRepository questionAttemptRepository,
            UserRepository userRepository,
            SkillProfileService skillProfileService
    ) {
        this.questionAttemptRepository =
                questionAttemptRepository;

        this.userRepository =
                userRepository;

        this.skillProfileService =
                skillProfileService;
    }

    public DashboardResponse getDashboard(
            int userId
    ) {

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow();

        List<QuestionAttempt> attempts =
                questionAttemptRepository
                        .findByUserIdOrderByCreatedAtDesc(
                                userId
                        );

        List<UserSkill> skills =
                skillProfileService.getSkills(
                        userId
                );

        DashboardResponse response =
                new DashboardResponse();

        response.setUserName(
                user.getUserName()
        );

        response.setLeetcodeVerified(
                user.isLeetcodeVerified()
        );

        response.setTotalAttempts(
                attempts.size()
        );

        response.setQuestionsSolved(
                countSolvedQuestions(
                        attempts
                )
        );

        response.setAnalyzedAttempts(
                countAnalyzedAttempts(
                        attempts
                )
        );

        response.setStrongTopics(
                getStrongTopics(
                        skills
                )
        );

        response.setWeakTopics(
                getWeakTopics(
                        skills
                )
        );

        response.setUnexploredTopics(
                getUnexploredTopics(
                        skills
                )
        );

        return response;
    }

    private int countSolvedQuestions(
            List<QuestionAttempt> attempts
    ) {

        List<String> titles =
                new ArrayList<>();

        for (QuestionAttempt attempt :
                attempts) {

            if (
                    !titles.contains(
                            attempt.getQuestionSlug()
                    )
            ) {

                titles.add(
                        attempt.getQuestionSlug()
                );
            }
        }

        return titles.size();
    }

    private int countAnalyzedAttempts(
            List<QuestionAttempt> attempts
    ) {

        int count = 0;

        for (QuestionAttempt attempt :
                attempts) {

            if (
                    Boolean.TRUE.equals(
                            attempt.getAnalysisCompleted()
                    )
            ) {

                count++;
            }
        }

        return count;
    }

    private List<TopicMastery> getStrongTopics(
            List<UserSkill> skills
    ) {

        List<UserSkill> attempted =
                new ArrayList<>();

        for (UserSkill skill :
                skills) {

            if (skill.getAttempts() > 0) {
                attempted.add(skill);
            }
        }

        attempted.sort(
                Comparator.comparingDouble(
                        UserSkill::getRating
                ).reversed()
        );

        List<TopicMastery> result =
                new ArrayList<>();

        for (UserSkill skill :
                attempted) {

            result.add(
                    new TopicMastery(
                            skill.getSkill(),
                            skill.getRating()
                    )
            );
        }

        return result;
    }

    private List<TopicMastery> getWeakTopics(
            List<UserSkill> skills
    ) {

        List<UserSkill> attempted =
                new ArrayList<>();

        for (UserSkill skill :
                skills) {

            if (skill.getAttempts() > 0) {
                attempted.add(skill);
            }
        }

        attempted.sort(
                Comparator.comparingDouble(
                        UserSkill::getRating
                )
        );

        List<TopicMastery> result =
                new ArrayList<>();

        for (UserSkill skill :
                attempted) {

            result.add(
                    new TopicMastery(
                            skill.getSkill(),
                            skill.getRating()
                    )
            );
        }

        return result;
    }

    private List<String> getUnexploredTopics(
            List<UserSkill> skills
    ) {

        List<String> result =
                new ArrayList<>();

        for (UserSkill skill :
                skills) {

            if (skill.getAttempts() == 0) {

                result.add(
                        skill.getSkill()
                );
            }
        }

        return result;
    }
}