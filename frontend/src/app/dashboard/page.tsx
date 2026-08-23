"use client";

import { useEffect, useState } from "react";

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

import {
  RecommendationResponse,
  SavedRecommendation,
} from "@/types/Recommendation";

const STORAGE_KEY = "unsheet_recommendation";

export default function DashboardPage() {
  const [status, setStatus] = useState<"idle" | "loading" | "generated">(
    "idle",
  );

  const [recommendation, setRecommendation] =
    useState<RecommendationResponse | null>(null);

  const [savedPrep, setSavedPrep] = useState<Omit<
    SavedRecommendation,
    "recommendation"
  > | null>(null);

  useEffect(() => {
    const saved = localStorage.getItem(STORAGE_KEY);

    if (!saved) {
      return;
    }

    try {
      const data: SavedRecommendation = JSON.parse(saved);

      if (data.recommendation && data.recommendation.questions) {
        setRecommendation(data.recommendation);

        setSavedPrep({
          mode: data.mode,
          company: data.company,
          interviewScheduled: data.interviewScheduled,
          daysRemaining: data.daysRemaining,
        });

        setStatus("generated");
      }
    } catch (error) {
      console.error("Failed to restore recommendation:", error);

      localStorage.removeItem(STORAGE_KEY);
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
          daysRemaining: Number(data.daysRemaining) || 0,
        });
      }

      setRecommendation(result);

      const savedData: SavedRecommendation = {
        recommendation: result,
        mode: data.mode,
        company: data.company || "",
        interviewScheduled: data.interviewScheduled || false,
        daysRemaining: data.daysRemaining ?? null,
      };

      localStorage.setItem(STORAGE_KEY, JSON.stringify(savedData));

      setSavedPrep({
        mode: savedData.mode,
        company: savedData.company,
        interviewScheduled: savedData.interviewScheduled,
        daysRemaining: savedData.daysRemaining,
      });

      setStatus("generated");
    } catch (error) {
      console.error(error);
      setStatus("idle");
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
                hasRecommendation={status === "generated" && !!recommendation}
                savedPrep={savedPrep}
              />

              {status === "idle" && <EmptyState />}

              {status === "loading" && <GeneratingState />}

              {status === "generated" && recommendation && (
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
