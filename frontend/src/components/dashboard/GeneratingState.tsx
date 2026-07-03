"use client";

import { Check, LoaderCircle } from "lucide-react";
import { motion } from "framer-motion";
import { useEffect, useState } from "react";

const steps = [
  "Reading previous attempts",
  "Finding weak concepts",
  "Optimizing for remaining days",
  "Building interview roadmap",
];

export default function GeneratingState() {
  const [currentStep, setCurrentStep] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentStep((prev) => {
        if (prev === steps.length) {
          clearInterval(interval);
          return prev;
        }

        return prev + 1;
      });
    }, 450);

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="flex h-[430px] items-center justify-center">
      <div className="w-full max-w-lg">
        <motion.h2
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="mb-10 text-center text-3xl font-bold"
        >
          Building your roadmap...
        </motion.h2>

        <div className="space-y-5">
          {steps.map((step, index) => (
            <motion.div
              key={step}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="flex items-center gap-4"
            >
              {index < currentStep ? (
                <Check size={18} className="text-green-400" />
              ) : index === currentStep ? (
                <LoaderCircle
                  size={18}
                  className="animate-spin text-cyan-400"
                />
              ) : (
                <div className="h-4 w-4 rounded-full border border-white/20" />
              )}

              <span className="text-lg text-white/75">{step}</span>
            </motion.div>
          ))}
        </div>
      </div>
    </div>
  );
}
