"use client";
import api from "@/lib/api";
import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { env } from "@/config/env";

export default function AuthSuccessPage() {
  const router = useRouter();

  const EXTENSION_ID = env.EXTENSION_ID;

  useEffect(() => {
    async function login() {
      await api.get("/auth/me");

      const { data } = await api.get("/auth/extension-token");

      if (window.chrome?.runtime) {
        window.chrome.runtime.sendMessage(
          EXTENSION_ID!,

          {
            type: "SET_AUTH_TOKEN",

            token: data.token,
          },
        );
      }

      router.replace("/dashboard");
    }

    login();
  }, []);

  return (
    <div className="flex min-h-screen items-center justify-center text-white">
      Logging you in...
    </div>
  );
}
