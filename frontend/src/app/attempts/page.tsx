"use client";

import { useEffect, useState } from "react";

import Link from "next/link";

import { ArrowLeft, Search, Loader2, History } from "lucide-react";

import AuthGuard from "@/app/auth/AuthGuard";

import Sidebar from "@/components/dashboard/Sidebar";
import DashboardHeader from "@/components/dashboard/DashboardHeader";

import AttemptCard from "@/components/attempts/AttemptCard";

import { getMyAttempts } from "@/services/attempt";

import { QuestionAttempt } from "@/types/Attempt";

export default function AttemptsPage() {
  const [attempts, setAttempts] = useState<QuestionAttempt[]>([]);

  const [loading, setLoading] = useState(true);

  const [search, setSearch] = useState("");

  useEffect(() => {
    async function loadAttempts() {
      try {
        const data = await getMyAttempts();

        setAttempts(data);
      } catch (error) {
        console.error("Failed to load attempts:", error);
      } finally {
        setLoading(false);
      }
    }

    loadAttempts();
  }, []);

  const filteredAttempts = attempts.filter((attempt) =>
    attempt.title.toLowerCase().includes(search.toLowerCase()),
  );

  return (
    <AuthGuard>
      <main className="flex min-h-screen bg-[#050608] text-white">
        <Sidebar />

        <div className="flex-1">
          <DashboardHeader />

          <div className="mx-auto max-w-5xl p-6 lg:p-10">
            <Link
              href="/dashboard"
              className="mb-8 inline-flex items-center gap-2 text-sm text-white/35 transition hover:text-white"
            >
              <ArrowLeft size={16} />
              Dashboard
            </Link>

            <div className="flex flex-col gap-5 sm:flex-row sm:items-end sm:justify-between">
              <div>
                <div className="flex items-center gap-3">
                  <History size={24} className="text-cyan-400" />

                  <h1 className="text-3xl font-semibold">My Attempts</h1>
                </div>

                <p className="mt-3 max-w-xl text-sm leading-6 text-white/35">
                  Review how your solutions evolved across every problem you
                  attempted.
                </p>
              </div>

              <div className="relative w-full sm:w-72">
                <Search
                  size={17}
                  className="absolute left-4 top-3.5 text-white/25"
                />

                <input
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                  placeholder="Search attempts..."
                  className="w-full rounded-xl border border-white/10 bg-[#0B0D11] py-3 pl-11 pr-4 text-sm text-white outline-none placeholder:text-white/20 focus:border-white/20"
                />
              </div>
            </div>

            <div className="mt-8">
              {loading && (
                <div className="flex items-center justify-center py-20 text-white/30">
                  <Loader2 size={22} className="mr-3 animate-spin" />
                  Loading attempts...
                </div>
              )}

              {!loading && filteredAttempts.length === 0 && (
                <div className="rounded-3xl border border-white/10 bg-[#0B0D11] p-12 text-center">
                  <p className="text-white/50">No attempts found.</p>

                  <p className="mt-2 text-sm text-white/25">
                    Solve a LeetCode problem using unSheet and your journey will
                    appear here.
                  </p>
                </div>
              )}

              {!loading && filteredAttempts.length > 0 && (
                <div className="space-y-4">
                  {filteredAttempts.map((attempt) => (
                    <AttemptCard key={attempt.id} attempt={attempt} />
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </main>
    </AuthGuard>
  );
}
