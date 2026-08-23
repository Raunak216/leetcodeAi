import { useState } from "react";
import {
  Play,
  MinusCircle,
  GitCommitHorizontal,
  ChevronDown,
} from "lucide-react";
import { AttemptStep } from "@/types/Attempt";
import AiInsights from "./AiInsights";

interface Props {
  attemptId: number;
  steps: AttemptStep[];
  startedAt: string;
}

function getStatusAbbreviation(verdict: string) {
  if (verdict === "Accepted")
    return {
      text: "AC",
      color: "text-[#00E599]",
      border: "border-[#00E599]/30",
      bg: "bg-[#00E599]",
    };
  if (verdict === "Compile Error")
    return {
      text: "CE",
      color: "text-[#FF5C5C]",
      border: "border-[#FF5C5C]/30",
      bg: "bg-[#FF5C5C]",
    };
  if (verdict === "Wrong Answer")
    return {
      text: "WA",
      color: "text-[#FFB84D]",
      border: "border-[#FFB84D]/30",
      bg: "bg-[#FFB84D]",
    };
  if (verdict === "Time Limit Exceeded")
    return {
      text: "TLE",
      color: "text-[#FFB84D]",
      border: "border-[#FFB84D]/30",
      bg: "bg-[#FFB84D]",
    };
  return {
    text: "ERR",
    color: "text-white/50",
    border: "border-white/20",
    bg: "bg-white/50",
  };
}

function getTimeDelta(start: string, current: string) {
  const diffMs = new Date(current).getTime() - new Date(start).getTime();
  const mins = Math.floor(diffMs / 60000);
  const secs = Math.floor((diffMs % 60000) / 1000);
  if (mins > 0) return `+${mins}m ${secs}s`;
  return `+${secs}s`;
}

// Parses raw code diff into sequential blocks of Removed -> Added hunks
function parseDiffHunks(diff: string, isInitial: boolean) {
  const lines = (diff || "").split("\n").filter(Boolean);

  if (isInitial) {
    return [
      {
        removed: [],
        added: lines.map((l) =>
          l.startsWith("+") || l.startsWith("-") ? l.substring(1) : l,
        ),
      },
    ];
  }

  const hunks: { removed: string[]; added: string[] }[] = [];
  let currentRemoved: string[] = [];
  let currentAdded: string[] = [];

  for (const line of lines) {
    if (line.startsWith("-")) {
      // If we see a removal but we already have additions queued, close the hunk and start a new one
      if (currentAdded.length > 0) {
        hunks.push({ removed: currentRemoved, added: currentAdded });
        currentRemoved = [];
        currentAdded = [];
      }
      currentRemoved.push(line.substring(1));
    } else if (line.startsWith("+")) {
      currentAdded.push(line.substring(1));
    }
  }

  if (currentRemoved.length > 0 || currentAdded.length > 0) {
    hunks.push({ removed: currentRemoved, added: currentAdded });
  }

  return hunks;
}

export default function SolutionEvolution({
  attemptId,
  steps,
  startedAt,
}: Props) {
  const [activeIndex, setActiveIndex] = useState(0);

  if (!steps.length) return null;

  const activeStep = steps[activeIndex];
  const isInitial = activeIndex === 0;
  const statusInfo = getStatusAbbreviation(activeStep.verdict);
  const hasTests =
    activeStep.passedTestCases != null && activeStep.totalTestCases != null;
  const percent = hasTests
    ? (activeStep.passedTestCases! / activeStep.totalTestCases!) * 100
    : 0;

  const hunks = parseDiffHunks(activeStep.codeDiff, isInitial);

  return (
    <div className="flex flex-col lg:flex-row gap-10 border-t border-white/5 pt-10">
      {/* Left Column: Journey Timeline */}
      <div className="w-full lg:w-48 shrink-0 relative">
        <h3 className="text-[11px] font-bold uppercase tracking-wider text-white/40 mb-6">
          Journey
        </h3>

        <div className="absolute left-[15px] top-12 bottom-0 w-px bg-white/10" />

        <div className="space-y-6 relative z-10">
          {steps.map((step, index) => {
            const status = getStatusAbbreviation(step.verdict);
            const isActive = index === activeIndex;
            const isSubmit = step.eventType === "SUBMIT";

            return (
              <button
                key={index}
                onClick={() => setActiveIndex(index)}
                className={`flex w-full items-start gap-4 text-left transition-opacity ${isActive ? "opacity-100" : "opacity-40 hover:opacity-70"}`}
              >
                <div
                  className={`mt-0.5 flex h-[30px] w-[30px] shrink-0 items-center justify-center rounded-full border bg-[#050505] ${status.border}`}
                >
                  {isSubmit ? (
                    <MinusCircle size={14} className={status.color} />
                  ) : (
                    <Play
                      size={12}
                      className={`ml-0.5 ${status.color}`}
                      fill="currentColor"
                    />
                  )}
                </div>

                <div className="flex-1 min-w-0 pt-0.5">
                  <div className="flex items-center gap-2 font-mono text-[13px]">
                    <span className={`font-bold ${status.color}`}>
                      {status.text}
                    </span>
                    <span className="text-white/30">{step.eventType}</span>
                  </div>

                  {step.passedTestCases != null && (
                    <div className="text-[11px] text-white/40 mt-1 font-mono">
                      {step.passedTestCases}/{step.totalTestCases} tests
                    </div>
                  )}

                  <div className="text-[11px] text-white/30 mt-0.5 font-mono">
                    {getTimeDelta(startedAt, step.timestamp)}
                  </div>
                </div>
              </button>
            );
          })}
        </div>
      </div>

      {/* Right Column: Code Detail Viewer & AI */}
      <div className="flex-1 min-w-0 flex flex-col gap-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div
              className="w-2 h-2 rounded-full"
              style={{
                backgroundColor: statusInfo.color
                  .replace("text-[", "")
                  .replace("]", ""),
              }}
            />
            <h2 className="text-lg font-bold text-white">
              Step {activeIndex + 1}
            </h2>
            <span
              className={`rounded-full border px-3 py-0.5 text-xs font-mono bg-opacity-10 ${statusInfo.border} ${statusInfo.color}`}
            >
              {activeStep.verdict}
            </span>
            <span className="text-xs font-mono text-white/30 uppercase tracking-widest">
              {activeStep.eventType}
            </span>
          </div>

          {hasTests && (
            <div className="text-xs font-mono text-white/40">
              <span className={statusInfo.color}>
                {activeStep.passedTestCases}
              </span>
              /{activeStep.totalTestCases} test cases
            </div>
          )}
        </div>

        {/* Dynamic Hunk Viewer */}
        {hunks.length > 0 ? (
          <div className="space-y-5">
            {hunks.map((hunk, idx) => (
              <div
                key={idx}
                className="rounded-xl border border-white/5 bg-[#0C1210] overflow-hidden shadow-2xl"
              >
                {hunk.removed.length > 0 && (
                  <div className="bg-[#FF5C5C]/[0.04]">
                    <div className="flex items-center gap-2 border-b border-[#FF5C5C]/10 px-4 py-2.5">
                      <div className="h-1.5 w-1.5 rounded-full bg-[#FF5C5C]" />
                      <span className="text-[10px] font-bold uppercase tracking-wider text-[#FF5C5C]">
                        Removed
                      </span>
                    </div>
                    <div className="p-4 overflow-x-auto custom-scrollbar">
                      <pre className="font-mono text-[13px] leading-relaxed text-[#FF5C5C]/80">
                        <code>{hunk.removed.join("\n")}</code>
                      </pre>
                    </div>
                  </div>
                )}

                {hunk.removed.length > 0 && hunk.added.length > 0 && (
                  <div className="relative flex justify-center border-t border-b border-white/5 bg-[#0C1210] py-1">
                    <ChevronDown size={14} className="text-white/20" />
                  </div>
                )}

                {hunk.added.length > 0 && (
                  <div className="bg-[#00D8D6]/[0.04]">
                    <div className="flex items-center gap-2 border-b border-[#00D8D6]/10 px-4 py-2.5">
                      <div className="h-1.5 w-1.5 rounded-full bg-[#00D8D6]" />
                      <span className="text-[10px] font-bold uppercase tracking-wider text-[#00D8D6]">
                        {isInitial ? "Initial Code Written" : "Added"}
                      </span>
                    </div>
                    <div className="p-4 overflow-x-auto custom-scrollbar">
                      <pre className="font-mono text-[13px] leading-relaxed text-[#00D8D6]/80">
                        <code>{hunk.added.join("\n")}</code>
                      </pre>
                    </div>
                  </div>
                )}
              </div>
            ))}
          </div>
        ) : (
          <div className="rounded-xl border border-white/5 bg-[#0C1210] p-6 shadow-2xl">
            <div className="flex items-center gap-3 text-sm font-semibold text-white/70">
              <GitCommitHorizontal size={18} className="text-white/40" />
              Submitted without changes
            </div>
            <p className="mt-2 text-[13px] text-white/40 leading-relaxed">
              The code from the previous step was submitted as-is for evaluation
              against all test cases.
            </p>

            {hasTests && (
              <div className="mt-6 flex items-center gap-4">
                <div className="h-2 flex-1 overflow-hidden rounded-full bg-white/10">
                  <div
                    className={`h-full rounded-full ${statusInfo.bg}`}
                    style={{ width: `${percent}%` }}
                  />
                </div>
                <div className="text-sm font-mono shrink-0">
                  <span className={`font-bold ${statusInfo.color}`}>
                    {activeStep.passedTestCases}
                  </span>
                  <span className="text-white/40">
                    /{activeStep.totalTestCases} test cases
                  </span>
                </div>
              </div>
            )}
          </div>
        )}

        <AiInsights attemptId={attemptId} />
      </div>
    </div>
  );
}
