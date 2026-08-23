// src/components/attempts/AttemptCard.tsx
import Link from "next/link";
import { ArrowRight, CheckCircle2, Clock3, Code2, XCircle } from "lucide-react";
import { QuestionAttempt } from "@/types/Attempt";

interface Props {
  attempt: QuestionAttempt;
}

const difficultyClass: Record<string, string> = {
  Easy: "text-emerald-400 bg-emerald-400/10",
  Medium: "text-yellow-400 bg-yellow-400/10",
  Hard: "text-red-400 bg-red-400/10",
};

export default function AttemptCard({ attempt }: Props) {
  let journey = null;
  try {
    journey = JSON.parse(attempt.journeyJson);
  } catch {
    // Handle quietly
  }

  const steps = journey?.steps ?? [];
  const runCount = steps.filter((step: any) => step.eventType === "RUN").length;

  return (
    <Link
      href={`/attempts/${attempt.id}`}
      className="group flex flex-col justify-between gap-4 border-b border-white/5 py-6 px-4 -mx-4 transition-colors hover:bg-white/[0.02] sm:flex-row sm:items-center rounded-xl sm:rounded-none"
    >
      <div className="min-w-0 flex-1">
        <div className="flex items-center gap-3">
          <h2 className="truncate text-lg font-medium text-white group-hover:text-cyan-400 transition-colors">
            {attempt.title}
          </h2>
          <span
            className={`rounded px-2 py-0.5 text-[11px] font-semibold uppercase tracking-wider ${
              difficultyClass[attempt.difficulty] ?? "bg-white/5 text-white/50"
            }`}
          >
            {attempt.difficulty}
          </span>
        </div>

        <div className="mt-2 flex flex-wrap items-center gap-4 text-xs text-white/40">
          <span className="flex items-center gap-1.5">
            <Code2 size={14} />
            {attempt.language}
          </span>
          <span className="flex items-center gap-1.5">
            <Clock3 size={14} />
            {attempt.runtime ? `${attempt.runtime} ms` : "N/A"}
          </span>
          <span>
            {runCount} {runCount === 1 ? "run" : "runs"}
          </span>
          <span className="hidden sm:inline">•</span>
          <span>{new Date(attempt.createdAt).toLocaleDateString()}</span>
        </div>
      </div>

      <div className="flex items-center justify-between sm:justify-end gap-5">
        {attempt.accepted ? (
          <span className="flex items-center gap-1.5 text-sm font-medium text-emerald-400">
            <CheckCircle2 size={16} />
            Accepted
          </span>
        ) : (
          <span className="flex items-center gap-1.5 text-sm font-medium text-white/40">
            <XCircle size={16} />
            Failed
          </span>
        )}
        <ArrowRight
          size={18}
          className="text-white/20 transition-transform group-hover:translate-x-1 group-hover:text-cyan-400 hidden sm:block"
        />
      </div>
    </Link>
  );
}
