package com.raunak.backend.service;

import com.raunak.backend.DsaSkillTopics;
import com.raunak.backend.EngineeringSkillTopics;
import com.raunak.backend.ReasoningSkillTopics;
import com.raunak.backend.model.QuestionAttempt;
import org.springframework.stereotype.Service;
import com.raunak.backend.model.SkillProfile;
import java.util.List;
@Service
public class PromptBuilderService {

    public String buildAnalysisPrompt(
            QuestionAttempt attempt
    ) {

        return """
You are an expert competitive programming coach.

Analyze the coding journey.

IMPORTANT RULES:

1. Ignore isolated syntax mistakes.
2. Ignore missing semicolons.
3. Ignore temporary typos fixed immediately.
4. Do NOT punish a topic for a simple typo.
5. Focus on conceptual understanding.
6. Use only the allowed topics below.
7. Only include topics that are actually relevant.
8. Never invent topic names.
9. Return ONLY raw JSON.
10. Do NOT wrap JSON inside markdown.

Allowed DSA Topics:
"""
                + String.join(
                ", ",
                DsaSkillTopics.ALL_TOPICS
        )

                + """

Allowed Engineering Topics:
"""

                + String.join(
                ", ",
                EngineeringSkillTopics.ALL_TOPICS
        )

                + """

Allowed Reasoning Topics:
"""

                + String.join(
                ", ",
                ReasoningSkillTopics.ALL_TOPICS
        )

                + """

Allowed Signals:

EFFICIENT_SOLVE
CLEAN_SOLVE
STRUGGLE
MISTAKE

Return EXACTLY this schema:

{
  "summary":"string",

  "dsaSignals":{
      "arrays":"CLEAN_SOLVE"
  },

  "engineeringSignals":{
      "edge_case_miss":"STRUGGLE"
  },

  "reasoningSignals":{
      "incomplete_problem_understanding":"MISTAKE"
  }
}

Journey:
"""

                + attempt.getJourneyJson();
    }


    public String buildRecommendationPrompt(

            SkillProfile profile,

            List<String> solvedQuestions,

            boolean interviewScheduled,

            Integer daysRemaining
    ){
        StringBuilder prompt=new StringBuilder("""
You are an expert DSA mentor.

Analyze the user's profile.

Recommend 5 LeetCode questions.


Rules:

1. Focus on weak skills.
2. Cover unexplored topics.
3. Avoid recommending already solved questions.
4. Return ONLY valid JSON.
5. Use real LeetCode question titles.

Return ONLY valid JSON.

{
  "questions":[
    {
      "title":"Two Sum",
      "slug":"two-sum",
      "difficulty":"Easy",
      "topics":[
        "Arrays",
        "Hash Map"
      ],
      "reason":"Why this question is recommended.",
      "estimatedTime":"15-20 min"
    }
  ]
}
Difficulty must be EXACTLY one of:
Easy
Medium
Hard

Slug must be the official LeetCode slug.
Example:
Two Sum -> two-sum

DSA Profile:
"""
                + profile.getDsa()

                + """

Engineering Profile:
"""

                + profile.getEngineering()

                + """

Reasoning Profile:
"""

                + profile.getReasoning()

                + """

Previously Solved Questions:
"""

                + solvedQuestions);
        if(interviewScheduled){

            prompt.append(

                    "\nInterview in "

                            + daysRemaining

                            + " days."
            );

        }else{

            prompt.append(

                    "\nNo interview scheduled. Focus on long-term mastery."
            );
        }
        return prompt.toString();
    }

    public String buildCompanyRecommendationPrompt(
            SkillProfile profile,
            List<String> solvedQuestions,
            List<String> companyQuestions,
            String company,
            int daysRemaining
    )
    {

        return """
You are an elite DSA mentor.

Your goal is NOT to simply pick questions from the company list.

Your goal is to create the BEST interview preparation plan.

Inputs:

1. User skill profile.
2. User solved questions.
3. Historical company questions.

Rules:

1. Recommend EXACTLY 5 questions.
2. Avoid already solved questions.
3. Improve weak skills.
4. Increase topic breadth.
5. Use company questions only as signals.
6. You may recommend similar LeetCode questions not present in the company list.
7. Prioritize interview success.
8. Return ONLY valid JSON.



Return ONLY valid JSON.

{
  "questions":[
    {
      "title":"Two Sum",
      "slug":"two-sum",
      "difficulty":"Easy",
      "topics":[
        "Arrays",
        "Hash Map"
      ],
      "reason":"Why this question is recommended.",
      "estimatedTime":"15-20 min"
    }
  ]
}
Difficulty must be EXACTLY one of:
Easy
Medium
Hard

Slug must be the official LeetCode slug.
Example:
Two Sum -> two-sum

Target Company:
"""
                + company

                + """

Days Remaining:
"""

                + daysRemaining

                + """

DSA Profile:
"""

                + profile.getDsa()

                + """

Engineering Profile:
"""

                + profile.getEngineering()

                + """

Reasoning Profile:
"""

                + profile.getReasoning()

                + """

Solved Questions:
"""

                + solvedQuestions

                + """

Company Questions:
"""

                + companyQuestions;
    }
}