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
            List<String> solvedQuestions
    ) {

        return """
You are an expert DSA mentor.

Analyze the user's profile.

Recommend 10 LeetCode questions.

Rules:

1. Focus on weak skills.
2. Cover unexplored topics.
3. Avoid recommending already solved questions.
4. Return ONLY valid JSON.
5. Use real LeetCode question titles.

Return JSON:

{
  "recommendedQuestions":[
    "Question Name"
  ],
  "reasoning":"..."
}

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

                + solvedQuestions;
    }
}