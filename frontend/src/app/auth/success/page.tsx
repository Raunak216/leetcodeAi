"use client";

import api from "@/lib/api";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect } from "react";

export default function AuthSuccessPage() {
  const router = useRouter();

  const EXTENSION_ID = process.env.NEXT_PUBLIC_EXTENSION_ID!;
  console.log(EXTENSION_ID);
  console.log(EXTENSION_ID.length);
  useEffect(() => {
    const completeLogin = async () => {
      try {
        const response = await api.get(
          "/auth/extension-token",

          {
            withCredentials: true,
          },
        );

        const token = response.data.token;

        if (window.chrome && window.chrome.runtime) {
          window.chrome.runtime.sendMessage(
            EXTENSION_ID,
            {
              type: "SET_AUTH_TOKEN",
              token,
            },
            () => {
              if (window.chrome.runtime.lastError) {
                console.log("Extension not installed");
              }
            },
          );
        }

        if (!token) {
          router.replace("/");
          return;
        }

        await api.get("/auth/me");

        router.replace("/dashboard");
      } catch (e) {
        console.error(e);

        localStorage.removeItem("token");

        router.replace("/");
      }
    };

    completeLogin();
  }, [router]);

  return (
    <div className="flex min-h-screen items-center justify-center bg-[#050608] text-white">
      Logging you in...
    </div>
  );
}
