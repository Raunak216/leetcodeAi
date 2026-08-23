// src/components/attempts/CodeViewer.tsx
import { Play, GitCommitHorizontal } from "lucide-react";

interface Props {
  rawText: string;
  isInitial: boolean;
}

export default function CodeViewer({ rawText, isInitial }: Props) {
  if (!rawText || !rawText.trim()) {
    return (
      <div className="flex h-full items-center justify-center p-8 text-sm text-white/20">
        No code data recorded for this step.
      </div>
    );
  }

  const lines = rawText.split("\n");

  // Mode 1: First Attempt - Show full raw code
  if (isInitial) {
    // If your backend stores the first attempt with '+' prefixes, we strip them to render plain code.
    const cleanLines = lines.map((line) =>
      line.startsWith("+") || line.startsWith("-") ? line.substring(1) : line,
    );

    return (
      <div className="py-4 font-mono text-[13px] leading-relaxed">
        {cleanLines.map((line, idx) => (
          <div key={idx} className="flex px-4 hover:bg-white/[0.02]">
            <span className="w-8 shrink-0 select-none text-right text-white/20 pr-4">
              {idx + 1}
            </span>
            <span className="text-white/70 whitespace-pre">{line}</span>
          </div>
        ))}
      </div>
    );
  }

  // Mode 2: Subsequent Attempts - Show specific Code Diff
  const diffLines = lines.filter(
    (line) => line.startsWith("+") || line.startsWith("-"),
  );

  if (!diffLines.length) {
    return (
      <div className="flex h-full items-center justify-center p-8 text-sm text-white/20">
        No significant logic changes detected.
      </div>
    );
  }

  return (
    <div className="py-4 font-mono text-[13px] leading-relaxed">
      {diffLines.map((line, idx) => {
        const isAdded = line.startsWith("+");
        const cleanLine = line.substring(1);

        return (
          <div
            key={idx}
            className={`flex px-4 ${
              isAdded
                ? "bg-emerald-500/[0.08] text-emerald-300"
                : "bg-red-500/[0.08] text-red-300"
            }`}
          >
            <span
              className={`w-8 shrink-0 select-none text-right pr-4 ${isAdded ? "text-emerald-500/50" : "text-red-500/50"}`}
            >
              {isAdded ? "+" : "-"}
            </span>
            <span className="whitespace-pre">{cleanLine}</span>
          </div>
        );
      })}
    </div>
  );
}
