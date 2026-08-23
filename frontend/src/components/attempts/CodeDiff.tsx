// src/components/attempts/CodeDiff.tsx
import { FileCode2 } from "lucide-react";

interface Props {
  diff: string;
}

export default function CodeDiff({ diff }: Props) {
  if (!diff || !diff.trim()) {
    return (
      <div className="rounded-xl border border-white/5 bg-white/[0.02] px-4 py-3 text-sm text-white/30 italic">
        No code changes recorded for this step.
      </div>
    );
  }

  // Only grab the actual added/removed lines to keep it concise
  const lines = diff
    .split("\n")
    .filter((line) => line.startsWith("+") || line.startsWith("-"));

  if (!lines.length) return null;

  return (
    <div className="overflow-hidden rounded-xl border border-white/10 bg-[#08090C]">
      <div className="flex items-center gap-2 border-b border-white/5 px-4 py-2.5 text-xs text-white/40">
        <FileCode2 size={14} />
        Changes
      </div>

      <div className="overflow-x-auto py-2 font-mono text-[13px] leading-6">
        {lines.map((line, index) => {
          const isAdded = line.startsWith("+");
          const isRemoved = line.startsWith("-");
          const cleanLine = line.substring(1); // Remove the raw + or -

          return (
            <div
              key={`${index}-${line}`}
              className={`whitespace-pre px-4 ${
                isAdded
                  ? "bg-emerald-500/[0.12] text-emerald-300"
                  : isRemoved
                    ? "bg-red-500/[0.12] text-red-300"
                    : "text-white/50"
              }`}
            >
              <span className="mr-4 inline-block w-3 select-none text-right opacity-40">
                {isAdded ? "+" : isRemoved ? "-" : " "}
              </span>
              {cleanLine}
            </div>
          );
        })}
      </div>
    </div>
  );
}
