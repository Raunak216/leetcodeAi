"use client";

import { motion } from "motion/react";
import Logo from "../../components/common/Logo";

export default function HeroSection() {
  return (
    <section className="relative z-10 w-full px-5 pt-6 flex flex-col items-center">
      <div className="w-full  flex items-center justify-start pb-8">
        <motion.div
          initial={{ opacity: 0, x: -15 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.5 }}
        >
          <Logo />
        </motion.div>
      </div>

      <motion.div
        initial={{ opacity: 0, y: 25 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.15, duration: 0.5 }}
        className="mt-4 max-w-3xl text-center"
      >
        <h1 className="text-6xl font-bold leading-tight text-white">
          Know exactly what
          <br />
          <span className="bg-gradient-to-r from-cyan-400 to-violet-500 bg-clip-text text-transparent">
            to practice next.
          </span>
        </h1>

        <p className="mx-auto mt-8 max-w-2xl text-lg leading-8 text-white/35">
          Our system analyzes your coding history across 100+ DSA topics and
          recommends the highest ROI questions to maximize interview
          preparation.
        </p>
      </motion.div>
    </section>
  );
}
