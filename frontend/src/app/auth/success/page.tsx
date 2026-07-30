"use client";

import api from "@/lib/api";
import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { env } from "@/config/env";
import { motion } from "framer-motion";

export default function AuthSuccessPage() {
  const router = useRouter();
  const EXTENSION_ID = env.EXTENSION_ID;

  useEffect(() => {
    async function login() {
      try {
        await api.get("/auth/me");

        const { data } = await api.get("/auth/extension-token");

        if (window.chrome?.runtime && EXTENSION_ID) {
          window.chrome.runtime.sendMessage(EXTENSION_ID, {
            type: "SET_AUTH_TOKEN",
            token: data.token,
          });
        }
      } catch (err) {
        console.error("Auth sync error:", err);
      } finally {
        router.replace("/dashboard");
      }
    }

    login();
  }, [router, EXTENSION_ID]);

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-[#050608] text-white">
      <div className="relative flex items-center justify-center">
        {/* Outer glowing blur ring */}
        <div className="absolute h-20 w-20 rounded-full bg-gradient-to-r from-cyan-500 to-violet-500 blur-xl opacity-50 animate-pulse" />

        {/* Spinning Gradient Border Spinner */}
        <motion.div
          animate={{ rotate: 360 }}
          transition={{ repeat: Infinity, duration: 1.2, ease: "linear" }}
          className="h-16 w-16 rounded-full border-4 border-transparent border-t-cyan-400 border-r-violet-500"
        />

        {/* Inner Logo / Dot */}
        <div className="absolute h-3 w-3 rounded-full bg-cyan-400 shadow-[0_0_12px_#22d3ee]" />
      </div>

      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.2 }}
        className="mt-6 flex items-center gap-1.5 text-lg font-medium text-gray-300"
      >
        <span>Logging you in</span>
        <span className="flex gap-1">
          <motion.span
            animate={{ opacity: [0.2, 1, 0.2] }}
            transition={{ repeat: Infinity, duration: 1, times: [0, 0.5, 1] }}
          >
            .
          </motion.span>
          <motion.span
            animate={{ opacity: [0.2, 1, 0.2] }}
            transition={{
              repeat: Infinity,
              duration: 1,
              delay: 0.2,
              times: [0, 0.5, 1],
            }}
          >
            .
          </motion.span>
          <motion.span
            animate={{ opacity: [0.2, 1, 0.2] }}
            transition={{
              repeat: Infinity,
              duration: 1,
              delay: 0.4,
              times: [0, 0.5, 1],
            }}
          >
            .
          </motion.span>
        </span>
      </motion.div>
    </div>
  );
}
