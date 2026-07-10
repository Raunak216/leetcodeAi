"use client";

import { motion } from "framer-motion";
import QuestionCard from "./QuestionCard";

import { RecommendationResponse } from "@/types/Recommendation";

interface Props {
  recommendation: RecommendationResponse | null;
}

export default function RecommendationQueue({ recommendation }: Props) {
  if (!recommendation) return null;
  return (
    <div className="space-y-5">
      {recommendation.questions.map((question, index) => (
        <motion.div
          key={question.title}
          initial={{ opacity: 0, y: 25 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{
            delay: index * 0.08,
          }}
        >
          <QuestionCard
            index={index + 1}
            title={question.title}
            slug={question.slug}
            difficulty={question.difficulty}
            topics={question.topics}
            reason={question.reason}
            time={question.estimatedTime}
          />
        </motion.div>
      ))}
    </div>
  );
}
