"use client";

import { motion } from "framer-motion";
import QuestionCard from "./QuestionCard";

const questions = [
  {
    title: "Two Sum",
    difficulty: "Easy",
    topics: ["Arrays", "Hash Map"],
    reason: "Foundation question with extremely high interview frequency.",
    time: "~15 min",
  },
  {
    title: "Binary Tree Level Order Traversal",
    difficulty: "Medium",
    topics: ["Tree", "BFS"],
    reason: "Strengthens tree traversal patterns.",
    time: "~25 min",
  },
  {
    title: "Longest Increasing Subsequence",
    difficulty: "Medium",
    topics: ["DP", "Binary Search"],
    reason: "Highest ROI DP problem based on your attempts.",
    time: "~40 min",
  },
  {
    title: "Number of Islands",
    difficulty: "Medium",
    topics: ["Graph", "DFS"],
    reason: "Essential graph traversal problem.",
    time: "~30 min",
  },
  {
    title: "Merge K Sorted Lists",
    difficulty: "Hard",
    topics: ["Heap", "Linked List"],
    reason: "Recommended before advanced heap questions.",
    time: "~45 min",
  },
] as const;

export default function RecommendationQueue() {
  return (
    <div className="space-y-5">
      {questions.map((question, index) => (
        <motion.div
          key={question.title}
          initial={{ opacity: 0, y: 25 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{
            delay: index * 0.08,
          }}
        >
          <QuestionCard index={index + 1} {...question} />
        </motion.div>
      ))}
    </div>
  );
}
