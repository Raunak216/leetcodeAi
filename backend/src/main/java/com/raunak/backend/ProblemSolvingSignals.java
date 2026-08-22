package com.raunak.backend;

public class ProblemSolvingSignals {

    public static final String[] ALL_SIGNALS = {

            "pattern_recognition",
            "constraint_analysis",
            "ds_selection",
            "edge_case_handling",
            "code_readability",
            "debugging_efficiency"
    };

    public static boolean contains(String signal) {

        for (String s : ALL_SIGNALS) {

            if (s.equals(signal)) {
                return true;
            }
        }

        return false;
    }
}