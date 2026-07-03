"use client";

import { useState, useEffect } from "react";

import Sidebar from "@/components/dashboard/Sidebar";
import DashboardHeader from "@/components/dashboard/DashboardHeader";
import RecommendationPanel from "@/components/dashboard/RecommendationPanel";
import RecommendationQueue from "@/components/dashboard/RecommendationQueue";
import EmptyState from "@/components/dashboard/EmptyState";
import RightSidebar from "@/components/dashboard/RightSidebar";
import GeneratingState from "@/components/dashboard/GeneratingState";
import AuthGuard from "@/components/auth/AuthGuard";
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

  return (
    <AuthGuard>
      <main className="flex min-h-screen bg-[#050608] text-white">
        <Sidebar />

        <div className="flex-1">
          <DashboardHeader />

          <div className="flex gap-7 p-6">
            <div className="flex-1">
              <RecommendationPanel
                onGenerate={() => {
                  setStatus("loading");
                }}
              />

              {status === "idle" && <EmptyState />}

              {status === "loading" && <GeneratingState />}

              {status === "generated" && <RecommendationQueue />}
            </div>

            <RightSidebar />
          </div>
        </div>
      </main>
    </AuthGuard>
  );
}
