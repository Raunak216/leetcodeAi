"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { Loader2 } from "lucide-react";

import AuthGuard from "@/app/auth/AuthGuard";
import Sidebar from "@/components/dashboard/Sidebar";

import AttemptHeader from "@/components/attempts/AttemptHeader";
import SolutionEvolution from "@/components/attempts/SolutionEvolution";

import { getAttempt } from "@/services/attempt";
import { AttemptJourney, QuestionAttempt } from "@/types/Attempt";

interface Props {
  params: Promise<{
    id: string;
  }>;
}

export default function AttemptDetailPage({ params }: Props) {
  const [attempt, setAttempt] = useState<QuestionAttempt | null>(null);
  const [journey, setJourney] = useState<AttemptJourney | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    async function loadAttempt() {
      try {
        const { id } = await params;
        const data = await getAttempt(Number(id));
        setAttempt(data);

        const parsedJourney = JSON.parse(data.journeyJson) as AttemptJourney;
        setJourney(parsedJourney);
      } catch (error) {
        console.error("Failed to load attempt:", error);
        setError(true);
      } finally {
        setLoading(false);
      }
    }
    loadAttempt();
  }, [params]);

  return (
    <AuthGuard>
      <main className="flex min-h-screen bg-[#050505] text-white">
        <Sidebar />

        <div className="flex-1 overflow-x-hidden">
          <div className="mx-auto max-w-6xl p-6 lg:p-10 pt-10 lg:pt-14">
            {loading && (
              <div className="flex items-center justify-center py-24 text-white/30">
                <Loader2 size={22} className="mr-3 animate-spin" />
                Loading attempt...
              </div>
            )}

            {!loading && error && (
              <div className="rounded-2xl border border-red-500/10 bg-[#0B0D11] p-10 text-center">
                <p className="text-red-400">Unable to load this attempt.</p>
                <Link
                  href="/attempts"
                  className="mt-5 inline-block text-sm text-white/40 hover:text-white"
                >
                  Return to attempts
                </Link>
              </div>
            )}

            {!loading && !error && attempt && journey && (
              <div className="space-y-8">
                <AttemptHeader attempt={attempt} journey={journey} />
                <SolutionEvolution
                  attemptId={attempt.id}
                  steps={journey.steps}
                  startedAt={journey.startedAt}
                />
              </div>
            )}
          </div>
        </div>
      </main>
    </AuthGuard>
  );
}
