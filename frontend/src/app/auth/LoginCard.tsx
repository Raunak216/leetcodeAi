"use client";
import { motion } from "motion/react";
import HowItWorks from "./HowItWorks";
import { env } from "@/config/env";

export default function LoginCard() {
  const privacyPage = `${env.frontendUrl}/privacy`;
  const handleLogin = () => {
    window.location.href = `${env.backendUrl}/oauth2/authorization/google`;
  };
  return (
    <motion.div
      initial={{ opacity: 0, y: 25 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.3 }}
      className="mt-16 w-full max-w-md rounded-3xl border border-white/10 bg-white/[0.03] p-7 backdrop-blur-md"
    >
      <button
        onClick={handleLogin}
        className="flex w-full items-center justify-center gap-3 rounded-xl bg-white py-4 font-semibold text-black transition hover:scale-[1.01]"
      >
        <img src="/googleSvg.svg" className="h-5 w-5" alt="" />
        Continue with Google
      </button>

      <HowItWorks />

      <p className="mt-8 text-center text-xs text-white/20">
        By continuing you agree to our{" "}
        <a href={privacyPage} className="hover hover:text-cyan-500">
          Terms & Privacy Policy
        </a>
        .
      </p>
    </motion.div>
  );
}
