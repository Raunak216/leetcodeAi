import { Clock3, ExternalLink } from "lucide-react";

interface Props {
  index: number;
  title: string;
  difficulty: "Easy" | "Medium" | "Hard";
  topics: readonly string[];
  reason: string;
  time: string;
}

export default function QuestionCard({
  index,
  title,
  difficulty,
  topics,
  reason,
  time,
}: Props) {
  const badge = {
    Easy: "bg-green-500/10 text-green-400",
    Medium: "bg-yellow-500/10 text-yellow-400",
    Hard: "bg-red-500/10 text-red-400",
  };

  const line = {
    Easy: "bg-green-400",
    Medium: "bg-yellow-400",
    Hard: "bg-red-400",
  };

  return (
    <div className="relative overflow-hidden rounded-3xl border border-white/10 bg-[#0B0D11]">
      <div className={`absolute left-0 top-0 h-full w-1 ${line[difficulty]}`} />

      <div className="flex gap-5 p-6">
        <div className="flex h-12 w-12 items-center justify-center rounded-full border border-white/10 text-white/50">
          {index}
        </div>

        <div className="flex-1">
          <div className="flex items-center gap-3">
            <h2 className="text-2xl font-semibold">{title}</h2>

            <span
              className={`rounded-full px-3 py-1 text-sm ${badge[difficulty]}`}
            >
              {difficulty}
            </span>
          </div>

          <div className="mt-2 flex gap-3 text-sm text-white/30">
            {topics.map((topic) => (
              <span key={topic}>#{topic}</span>
            ))}
          </div>

          <p className="mt-4 text-white/45">✨ {reason}</p>
        </div>

        <div className="text-right">
          <div className="mb-5 flex items-center justify-end gap-2 text-white/30">
            <Clock3 size={15} />

            {time}
          </div>

          <button className="flex items-center gap-2 text-cyan-400">
            Solve
            <ExternalLink size={15} />
          </button>
        </div>
      </div>
    </div>
  );
}
