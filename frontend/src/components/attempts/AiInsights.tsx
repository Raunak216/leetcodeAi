import { useState } from "react";
import { Bot, Loader2 } from "lucide-react";
import { generateAttemptInsights } from "@/services/attempt";

interface Props {
  attemptId: number;
}

export default function AiInsights({ attemptId }: Props) {
  const [loading, setLoading] = useState(false);
  const [insights, setInsights] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handleGenerate = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await generateAttemptInsights(attemptId);
      setInsights(response.insights);
    } catch (error) {
      console.error(error);
      setError("Could not generate insights. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mt-4 rounded-xl border border-white/5 bg-[#0B0D11] p-6 shadow-2xl">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-3">
          <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-white/5 border border-white/10">
            <Bot size={18} className="text-white/60" />
          </div>
          <h2 className="text-lg font-bold text-white">AI Insights</h2>
        </div>
        <div className="text-[11px] font-mono font-bold tracking-widest uppercase text-white/20">
          unSheet
        </div>
      </div>

      {!insights && (
        <div className="flex flex-col items-start gap-4">
          <p className="text-[13px] text-white/40 leading-relaxed max-w-2xl">
            Get feedback on your problem-solving process and debugging journey.
          </p>
          <button
            onClick={handleGenerate}
            disabled={loading}
            className="flex items-center gap-2 rounded-lg bg-white px-4 py-2 text-[13px] font-semibold text-black transition hover:bg-white/80 disabled:cursor-not-allowed disabled:opacity-50"
          >
            {loading ? (
              <>
                <Loader2 size={16} className="animate-spin" />
                Analyzing...
              </>
            ) : (
              "Analyze"
            )}
          </button>
          {error && <p className="text-sm text-red-400">{error}</p>}
        </div>
      )}

      {insights && (
        <div className="text-[13px] leading-relaxed text-white/70 space-y-4 whitespace-pre-line border-t border-white/5 pt-5 mt-2">
          {insights}
        </div>
      )}
    </div>
  );
}
