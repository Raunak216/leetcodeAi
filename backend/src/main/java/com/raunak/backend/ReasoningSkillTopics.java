package com.raunak.backend;

public class ReasoningSkillTopics {
    public static boolean contains(
            String topic
    ) {
        for (
                String t :
                ALL_TOPICS
        ) {
            if (
                    t.equals(topic)
            ) {
                return true;
            }
        }

        return false;
    }
    public static final String[] ALL_TOPICS = {

            "problem_understanding",

            "constraint_analysis",

            "pattern_recognition",

            "decomposition",

            "abstraction",

            "implementation_planning",

            "edge_case_thinking",

            "debugging",

            "hypothesis_testing",

            "optimization_reasoning",

            "tradeoff_analysis",

            "state_tracking",

            "invariant_reasoning",

            "recursive_reasoning",

            "mathematical_reasoning"
    };
}
