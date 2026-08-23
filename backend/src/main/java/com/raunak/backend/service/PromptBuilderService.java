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
                You are a DSA interview coach reviewing a user's
                complete coding journey.
                
                Your goal is to help the user become better at solving
                DSA problems independently.
                
                QUESTION:
                %s
                
                DIFFICULTY:
                %s
                
                LANGUAGE:
                %s
                
                COMPLETE CODING JOURNEY:
                %s
                
                Give a concise but useful review of how the user solved
                this problem.
                
                Structure the response into exactly these sections:
                
                What went well
                <1-2 concise sentences>
                
                Where you struggled
                <1-2 concise sentences>
                
                How to improve
                <2-3 concise sentences with concrete advice>
                
                Pattern to remember
                <1-2 concise sentences describing the reusable DSA idea>
                
                Next step
                <1 concise sentence suggesting what to practice next>
                
                IMPORTANT RULES:
                
                1. Keep the entire response around 8-12 lines.
                
                2. Use short paragraphs with clear spacing between sections.
                
                3. Do NOT explain the entire problem solution.
                
                4. Do NOT reproduce the user's code.
                
                5. Do NOT describe every run individually.
                
                6. Focus on the user's actual solving behaviour visible
                   in the journey.
                
                7. Mention meaningful issues such as:
                   - wrong approach
                   - implementation mistakes
                   - compile errors
                   - wrong answers
                   - debugging
                   - edge cases
                   - unnecessary complexity
                   - good algorithmic decisions
                
                8. Give concrete DSA advice rather than generic advice.
                
                9. Do not mention numerical skill ratings.
                
                10. Do not mention internal mastery scores.
                
                11. Do not use JSON.
                
                12. Do not use markdown code blocks.
                
                Return only the final coaching review.
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
                Create a high-ROI interview preparation plan for the user.
                
                COMPANY:
                %s
                
                DAYS REMAINING:
                %d
                
                CURRENT SKILL PROFILE:
                %s
                
                QUESTIONS ALREADY SOLVED FROM THIS COMPANY:
                %s
                
                COMPANY QUESTION BANK:
                %s
                
                Your task is to recommend the most valuable questions
                for this user's remaining preparation time.
                
                You may recommend:
                - Questions directly from the company question bank.
                - Similar questions that test the same important pattern.
                - Questions that are not in the company question bank if they
                  provide better preparation for the user's weaknesses.
                
                Do not recommend questions already solved by the user.
                
                Prioritize high ROI questions based on:
                - Weak or under-practiced skills.
                - Important DSA patterns for the company.
                - Variety of patterns rather than repetitive questions.
                - The number of days remaining.
                - Interview usefulness.
                
                Return ONLY valid JSON in this exact format:
                
                {
                  "questions": [
                    {
                      "title": "Question title",
                      "slug": "leetcode-question-slug",
                      "difficulty": "Easy",
                      "topics": ["arrays", "hashing"],
                      "reason": "Good practice for hash-based lookup patterns.",
                      "estimatedTime": "20 min"
                    }
                  ]
                }
                
                Rules:
                
                1. Return only questions that are appropriate for interview
                   preparation.
                
                2. Only return 6 question at a time on run.
                
                3. Keep the reason short. Explain why this question is useful
                   for the user in one sentence.
                
                4. Do not mention numerical skill ratings, mastery scores,
                   or internal profile values in the reason.
                
                5. Do not say things such as "your skill is 52" or
                   "your score is low".
                
                6. Topics must use the provided DSA skill names when applicable.
                
                7. Difficulty must be exactly one of:
                   Easy, Medium, Hard.
                
                8. estimatedTime should be a simple estimate such as
                   "15 min", "25 min", or "40 min".
                
                9. Return the actual LeetCode slug when recommending
                   a LeetCode problem.
                
                10. Do not return explanations outside the JSON.
                
                Return only JSON.
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