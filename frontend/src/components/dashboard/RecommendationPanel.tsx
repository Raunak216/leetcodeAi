import { Brain, Building2, CalendarDays, Sparkles } from "lucide-react";
import { useState, useEffect } from "react";
import { getCompanies } from "@/services/company";
interface RecommendationPanelProps {
  onGenerate: (data: {
    mode: "general" | "company";

    company: string;

    interviewScheduled: boolean;

    daysRemaining: number | null;
  }) => void;

  disabled?: boolean;
}
export default function RecommendationPanel({
  onGenerate,
  disabled = false,
}: RecommendationPanelProps) {
  const [mode, setMode] = useState<"general" | "company">("general");
  const [companies, setCompanies] = useState([]);
  const [interviewScheduled, setInterviewScheduled] = useState(false);
  const [daysRemaining, setDaysRemaining] = useState("");
  const [company, setCompany] = useState("");
  useEffect(() => {
    getCompanies().then(setCompanies);
  }, []);

  return (
    <div className="mb-8 rounded-3xl bg-[#0B0D11] bg-white/[0.02] p-7">
      <p className="mb-6 text-xs uppercase tracking-[0.3em] text-white/25">
        What are you preparing for?
      </p>

      <div className="grid grid-cols-2 gap-4">
        <button
          onClick={() => setMode("general")}
          className={`rounded-2xl border p-6 text-left transition
          ${
            mode === "general"
              ? "border-cyan-500/30 bg-cyan-500/10"
              : "border-white/10 hover:border-white/20"
          }`}
        >
          <Brain className="mb-5 text-cyan-400" />

          <h3 className="text-xl font-semibold">General Prep</h3>

          <p className="mt-2 text-white/35">
            Personalized roadmap from all your attempts.
          </p>
        </button>

        <button
          onClick={() => setMode("company")}
          className={`rounded-2xl border p-6 text-left transition
          ${
            mode === "company"
              ? "border-cyan-500/30 bg-cyan-500/10"
              : "border-white/10 hover:border-white/20"
          }`}
        >
          <Building2 className="mb-5 text-white/40" />

          <h3 className="text-xl font-semibold">Company Prep</h3>

          <p className="mt-2 text-white/35">
            Target a specific company's interview pool.
          </p>
        </button>
      </div>

      <div className="mt-5 flex gap-4">
        {mode === "company" && (
          <select
            value={company}
            onChange={(e) => setCompany(e.target.value)}
            className="flex-1 rounded-xl border border-white/10 bg-[#0B0D11] px-4 py-3 outline-none"
          >
            <option value="">Select Company</option>

            {companies.map((company: any) => (
              <option key={company.id} value={company.name}>
                {company.name}
              </option>
            ))}
          </select>
        )}

        {(mode === "company" || interviewScheduled) && (
          <div className="relative w-52">
            <CalendarDays
              size={18}
              className="absolute left-4 top-4 text-white/35"
            />

            <input
              type="number"
              value={daysRemaining}
              onChange={(e) => setDaysRemaining(e.target.value)}
              placeholder="Days Remaining"
              className="w-full rounded-xl border border-white/10 bg-transparent py-3 pl-11 pr-4 outline-none placeholder:text-white/20"
            />
          </div>
        )}

        <button
          disabled={disabled}
          onClick={() =>
            onGenerate({
              mode,

              company,

              interviewScheduled,

              daysRemaining:
                mode === "company" || interviewScheduled
                  ? Number(daysRemaining)
                  : null,
            })
          }
          className="flex flex-1 items-center justify-center gap-3 rounded-xl bg-cyan-500 py-3 font-semibold text-black transition hover:bg-cyan-400 disabled:cursor-not-allowed disabled:opacity-50"
        >
          <Sparkles size={18} />
          Build Roadmap
        </button>
      </div>
    </div>
  );
}
