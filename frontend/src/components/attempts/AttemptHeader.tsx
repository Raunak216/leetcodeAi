import Link from "next/link";
import { Clock3, Layers, Zap, Cpu, ArrowLeft } from "lucide-react";
import { QuestionAttempt, AttemptJourney } from "@/types/Attempt";

interface Props {
  attempt: QuestionAttempt;
  journey: AttemptJourney;
}

function calculateDuration(start: string, end?: string) {
  if (!start || !end) return "N/A";
  const diffMs = new Date(end).getTime() - new Date(start).getTime();
  if (isNaN(diffMs) || diffMs < 0) return "N/A";

  const m = Math.floor(diffMs / 60000);
  const s = Math.floor((diffMs % 60000) / 1000);

  if (m === 0) return `${s}s`;
  return `${m}m ${s}s`;
}

export default function AttemptHeader({ attempt, journey }: Props) {
  const duration = calculateDuration(
    journey.startedAt,
    journey.completedAt || journey.steps[journey.steps.length - 1]?.timestamp,
  );

  return (
    <div className="flex flex-col gap-8">
      {/* Top Title Area */}
      <div>
        <h1 className="text-3xl font-bold tracking-tight text-white">
          {attempt.title}
        </h1>
        <p className="mt-1 text-sm font-medium text-white/40">
          {attempt.questionSlug} • {journey.steps.length} steps
        </p>
      </div>

      {/* Breadcrumb row */}
      <div className="flex items-center gap-2 text-sm font-mono text-white/40">
        <Link
          href="/attempts"
          className="flex items-center hover:text-white transition-colors"
        >
          <ArrowLeft size={14} className="mr-2" />
          All attempts
        </Link>
        <span className="text-white/20">|</span>
        <span>{attempt.questionSlug}</span>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
        {/* Duration Card */}
        <div className="rounded-2xl border border-white/5 bg-[#0C0C0E] p-5">
          <div className="flex items-center gap-2 text-[11px] font-bold uppercase tracking-wider text-cyan-400">
            <Clock3 size={14} />
            Duration
          </div>
          <div className="mt-3 text-3xl font-bold text-white">{duration}</div>
        </div>

        {/* Steps Card */}
        <div className="rounded-2xl border border-white/5 bg-[#0C0C0E] p-5">
          <div className="flex items-center gap-2 text-[11px] font-bold uppercase tracking-wider text-purple-400">
            <Layers size={14} />
            Steps
          </div>
          <div className="mt-3 text-3xl font-bold text-white">
            {journey.steps.length} steps
          </div>
        </div>

        {/* Runtime Card */}
        <div className="rounded-2xl border border-white/5 bg-[#0C0C0E] p-5">
          <div className="flex items-center gap-2 text-[11px] font-bold uppercase tracking-wider text-orange-400">
            <Zap size={14} />
            Runtime
          </div>
          <div className="mt-3 text-3xl font-bold text-white">
            {attempt.runtime !== null ? `${attempt.runtime} ms` : "N/A"}
          </div>
        </div>

        {/* Memory Card */}
        <div className="rounded-2xl border border-white/5 bg-[#0C0C0E] p-5">
          <div className="flex items-center gap-2 text-[11px] font-bold uppercase tracking-wider text-emerald-400">
            <Cpu size={14} />
            Memory
          </div>
          <div className="mt-3 text-3xl font-bold text-white">
            {attempt.memory !== null
              ? `${(attempt.memory / 1024 / 1024).toFixed(1)} MB`
              : "N/A"}
          </div>
        </div>
      </div>
    </div>
  );
}
