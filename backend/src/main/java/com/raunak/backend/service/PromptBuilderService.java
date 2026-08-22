package com.raunak.backend.service;

import com.raunak.backend.DsaSkillTopics;
import com.raunak.backend.ProblemSolvingSignals;
import com.raunak.backend.model.QuestionAttempt;
import com.raunak.backend.model.UserSkill;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptBuilderService {

    public String buildSkillDeltaPrompt(
            QuestionAttempt attempt
    ) {

        String allowedSkills =
                String.join(
                        ", ",
                        DsaSkillTopics.ALL_TOPICS
                )
                        + ", "
                        + String.join(
                        ", ",
                        ProblemSolvingSignals.ALL_SIGNALS
                );

        return """
                Analyze this coding attempt.
                
                Your job has two parts:
                
                1. Give a quality score for the user's final solution
                   and reasoning.
                2. Identify the 1 to 3 skills that were genuinely
                   exercised by this attempt.
                
                Question:
                %s
                
                Difficulty:
                %s
                
                Language:
                %s
                
                Compile Errors:
                %d
                
                Logic Failures:
                %d
                
                Accepted:
                %s
                
                Thinking Journey:
                %s
                
                Return ONLY valid JSON:
                
                {
                  "score": 0.85,
                  "tags": ["graph_bfs", "queue"]
                }
                
                Rules:
                
                1. score must be between 0.0 and 1.0.
                
                2. The score represents the quality of the final
                   solution and reasoning.
                
                3. Do not directly apply penalties for compile errors,
                   wrong answers, or TLE to the score. The backend
                   calculates those penalties separately.
                
                4. Return between 1 and 3 tags when there is enough
                   evidence.
                
                5. Only tag skills that were substantively required
                   and demonstrated by the algorithmic reasoning.
                
                6. Do not tag a skill just because its data structure
                   or syntax appeared in the code.
                
                7. For example, using a 2D array for visited state
                   does not automatically mean the user practiced
                   the arrays skill.
                
                8. Do not invent skill names.
                
                9. Use only these skill names:
                
                %s
                
                10. Do not return skill ratings.
                
                11. Do not return skill deltas.
                
                12. Do not return explanations.
                
                13. Return only JSON.
                """.formatted(
                attempt.getTitle(),
                attempt.getDifficulty(),
                attempt.getLanguage(),
                attempt.getCompileErrors(),
                attempt.getLogicFailures(),
                attempt.getAccepted(),
                attempt.getJourneyJson(),
                allowedSkills
        );
    }

    public String buildDetailedInsightsPrompt(
            QuestionAttempt attempt
    ) {

        return """
                Provide a detailed coaching analysis of this coding attempt.
                
                Question:
                %s
                
                Difficulty:
                %s
                
                Language:
                %s
                
                Thinking Journey:
                %s
                
                Analyze the complete reasoning journey.
                
                Cover:
                
                1. Initial approach.
                
                2. How the approach evolved.
                
                3. Important mistakes or wrong turns.
                
                4. What was done well.
                
                5. Why failed attempts failed.
                
                6. How the final approach works.
                
                7. Time complexity.
                
                8. Space complexity.
                
                9. Missed edge cases.
                
                10. Improvements to the reasoning process.
                
                11. The important pattern or concept to remember.
                
                Be specific to this attempt.
                Do not give generic motivational advice.
                Do not invent events that are not present in the
                thinking journey.
                """.formatted(
                attempt.getTitle(),
                attempt.getDifficulty(),
                attempt.getLanguage(),
                attempt.getJourneyJson()
        );
    }

    public String buildRecommendationPrompt(
            List<UserSkill> skills,
            List<String> solvedQuestions,
            boolean interviewScheduled,
            int daysRemaining
    ) {

        return """
                Recommend the highest ROI DSA questions for this user.
                
                CURRENT SKILL PROFILE:
                %s
                
                QUESTIONS ALREADY SOLVED:
                %s
                
                INTERVIEW SCHEDULED:
                %s
                
                DAYS REMAINING:
                %d
                
                Choose questions that provide the best improvement
                for the user's current weaknesses.
                
                Do not recommend questions already solved.
                
                Prioritize high-value patterns and skills that matter
                for interview preparation.
                
                Return ONLY valid JSON in this format:
                
                {
                  "recommendedQuestions": [
                    "Question Name 1",
                    "Question Name 2"
                  ],
                  "reasoning": "Short explanation"
                }
                """.formatted(
                buildSkillProfileText(skills),
                String.join(", ", solvedQuestions),
                interviewScheduled,
                daysRemaining
        );
    }

    public String buildCompanyRecommendationPrompt(
            List<UserSkill> skills,
            List<String> solvedQuestions,
            List<String> companyQuestions,
            String company,
            int daysRemaining
    ) {

        return """
                Create a high ROI interview preparation plan.
                
                COMPANY:
                %s
                
                DAYS REMAINING:
                %d
                
                CURRENT SKILL PROFILE:
                %s
                
                COMPANY QUESTIONS ALREADY SOLVED:
                %s
                
                AVAILABLE COMPANY QUESTIONS:
                %s
                
                Recommend the best questions from the available
                company question list.
                
                Prioritize questions that match the user's weak
                or under-practiced skills and are likely to provide
                strong interview preparation value.
                
                Do not recommend questions already solved.
                
                Return ONLY valid JSON in this format:
                
                {
                  "recommendedQuestions": [
                    "Question Name 1",
                    "Question Name 2"
                  ],
                  "reasoning": "Short explanation"
                }
                """.formatted(
                company,
                daysRemaining,
                buildSkillProfileText(skills),
                String.join(", ", solvedQuestions),
                String.join(", ", companyQuestions)
        );
    }

    private String buildSkillProfileText(
            List<UserSkill> skills
    ) {

        StringBuilder result =
                new StringBuilder();

        for (UserSkill skill : skills) {

            result.append(
                    skill.getSkill()
            );

            result.append(
                    ": rating="
            );

            result.append(
                    String.format(
                            "%.2f",
                            skill.getRating()
                    )
            );

            result.append(
                    ", attempts="
            );

            result.append(
                    skill.getAttempts()
            );

            result.append(
                    ", lastPracticedAt="
            );

            if (skill.getLastPracticedAt() == null) {

                result.append("never");

            } else {

                result.append(
                        skill.getLastPracticedAt()
                );
            }

            result.append("\n");
        }

        return result.toString();
    }
}