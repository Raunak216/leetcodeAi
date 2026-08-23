"use client";

import { useState, useEffect } from "react";

import Sidebar from "@/components/dashboard/Sidebar";
import DashboardHeader from "@/components/dashboard/DashboardHeader";
import RecommendationPanel from "@/components/dashboard/RecommendationPanel";
import RecommendationQueue from "@/components/dashboard/RecommendationQueue";
import EmptyState from "@/components/dashboard/EmptyState";
import RightSidebar from "@/components/dashboard/RightSidebar";
import GeneratingState from "@/components/dashboard/GeneratingState";
import AuthGuard from "@/app/auth/AuthGuard";
import {
  generateGeneralRecommendation,
  generateCompanyRecommendation,
} from "@/services/recommendation";

import { RecommendationResponse } from "@/types/Recommendation";

const STORAGE_KEY = "unsheet_recommendation";

export default function DashboardPage() {
  const [status, setStatus] = useState<"idle" | "loading" | "generated">(
    "idle",
  );

  const [recommendation, setRecommendation] =
    useState<RecommendationResponse | null>(null);

  useEffect(() => {
    const savedRecommendation = localStorage.getItem(STORAGE_KEY);

    if (savedRecommendation) {
      try {
        const parsed = JSON.parse(savedRecommendation);

        if (parsed && Array.isArray(parsed.questions)) {
          setRecommendation(parsed);
          setStatus("generated");
        }
      } catch (error) {
        console.error("Failed to load saved recommendation:", error);

        localStorage.removeItem(STORAGE_KEY);
      }
    }
  }, []);

  const handleGenerate = async (data: any) => {
    setStatus("loading");

    try {
      let result: RecommendationResponse;

      if (data.mode === "general") {
        result = await generateGeneralRecommendation({
          interviewScheduled: data.interviewScheduled,

          daysRemaining: data.daysRemaining,
        });
      } else {
        result = await generateCompanyRecommendation({
          company: data.company,

          daysRemaining: data.daysRemaining,
        });
      }

      setRecommendation(result);

      localStorage.setItem(STORAGE_KEY, JSON.stringify(result));

      setStatus("generated");
    } catch (error) {
      console.error(error);

      setStatus(recommendation ? "generated" : "idle");
    }
  };

  return (
    <AuthGuard>
      <main className="flex min-h-screen bg-[#050608] text-white">
        <Sidebar />

        <div className="flex-1">
          <DashboardHeader />

          <div className="flex gap-7 p-6">
            <div className="flex-1">
              <RecommendationPanel
                onGenerate={handleGenerate}
                disabled={status === "loading"}
              />

              {status === "idle" && <EmptyState />}

              {status === "loading" && <GeneratingState />}

              {status === "generated" && (
                <RecommendationQueue recommendation={recommendation} />
              )}
            </div>

            <RightSidebar />
          </div>
        </div>
      </main>
    </AuthGuard>
  );
}
