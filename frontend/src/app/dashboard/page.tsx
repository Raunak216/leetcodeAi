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
export default function DashboardPage() {
  const [status, setStatus] = useState<"idle" | "loading" | "generated">(
    "idle",
  );

  useEffect(() => {
    if (status === "loading") {
      const timer = setTimeout(() => {
        setStatus("generated");
      }, 2200);

      return () => clearTimeout(timer);
    }
  }, [status]);
  const [recommendation, setRecommendation] = useState<any>(null);

  const handleGenerate = async (data: any) => {
    setStatus("loading");

    try {
      if (data.mode === "general") {
        const result = await generateGeneralRecommendation({
          interviewScheduled: data.interviewScheduled,

          daysRemaining: data.daysRemaining,
        });

        setRecommendation(result);
      } else {
        const result = await generateCompanyRecommendation({
          company: data.company,

          daysRemaining: data.daysRemaining,
        });

        setRecommendation(result);
      }

      setStatus("generated");
    } catch (e) {
      console.error(e);

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
