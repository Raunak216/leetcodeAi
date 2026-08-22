package com.raunak.backend;

public class DsaSkillTopics {

    public static final String[] ALL_TOPICS = {

            "arrays",
            "matrix_mechanics",
            "prefix_sum",
            "difference_array",
            "bit_manipulation",
            "number_theory",
            "modular_arithmetic",
            "combinatorics",
            "geometry",

            "two_pointers",
            "sliding_window",
            "intervals",

            "sorting",
            "binary_search",
            "binary_search_answer",
            "sweep_line",
            "greedy",

            "hashing",
            "linked_list",
            "stack",
            "monotonic_stack",
            "queue",
            "deque",

            "strings",
            "trie",
            "rolling_hash",
            "kmp",

            "binary_tree",
            "tree_dfs",
            "tree_bfs",
            "bst",
            "lowest_common_ancestor",
            "heap",

            "graph_dfs",
            "graph_bfs",
            "connected_components",
            "topological_sort",
            "dijkstra",
            "bellman_ford",
            "floyd_warshall",
            "union_find",
            "minimum_spanning_tree",
            "bipartite_graph",
            "strongly_connected_components",

            "recursion",
            "backtracking",
            "memoization",
            "tabulation",
            "dp_1d",
            "dp_2d",
            "dp_strings",
            "dp_knapsack",
            "interval_dp",
            "dp_tree",
            "dp_bitmask",
            "digit_dp",

            "segment_tree",
            "fenwick_tree",
            "simulation",
            "design_problems"
    };

    public static boolean contains(String topic) {

        for (String t : ALL_TOPICS) {

            if (t.equals(topic)) {
                return true;
            }
        }

        return false;
    }
}