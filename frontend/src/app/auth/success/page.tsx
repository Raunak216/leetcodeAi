"use client";

import api from "@/lib/api";
import { useRouter, useSearchParams } from "next/navigation";
import { useEffect } from "react";

export default function AuthSuccessPage() {
  const router = useRouter();

  const params = useSearchParams();

  useEffect(() => {
    const completeLogin = async () => {
      try {
        const token = params.get("token");

        if (!token) {
          router.replace("/");
          return;
        }

        localStorage.setItem("token", token);

        await api.get("/auth/me");

        router.replace("/dashboard");
      } catch (e) {
        console.error(e);

        localStorage.removeItem("token");

        router.replace("/");
      }
    };

    completeLogin();
  }, [params, router]);

  return (
    <div className="flex min-h-screen items-center justify-center bg-[#050608] text-white">
      Logging you in...
    </div>
  );
}
