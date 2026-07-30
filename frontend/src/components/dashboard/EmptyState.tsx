"use client";

import { Code2, Terminal, Sparkles } from "lucide-react";
import { motion } from "framer-motion";

export default function EmptyState() {
  return (
    <div className="flex min-h-[420px] items-center justify-center p-6">
      <div className="flex flex-col items-center text-center">
        <motion.div
          initial={{ opacity: 0, y: 15 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.4 }}
          className="mt-8 max-w-md"
        >
          <h2 className="text-2xl font-bold tracking-tight text-white">
            Your queue is ready to generate
          </h2>

          <p className="mt-3 text-base leading-relaxed text-white/40">
            Select your preparation strategy above to let unSheet build the
            highest ROI roadmap for your interview.
          </p>
        </motion.div>
      </div>
    </div>
  );
}
