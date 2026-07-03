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

        const response = await api.get("/auth/me");

        localStorage.setItem("user", JSON.stringify(response.data));

        router.replace("/dashboard");
      } catch (error) {
        console.error(error);

        localStorage.removeItem("token");

        localStorage.removeItem("user");

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
