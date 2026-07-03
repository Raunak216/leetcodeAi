import { MessageSquare } from "lucide-react";

export default function AskAiCard() {
  return (
    <div className="rounded-3xl border border-violet-500/20 bg-violet-500/[0.03] p-6">
      <div className="flex items-center gap-3">
        <MessageSquare size={20} className="text-violet-400" />

        <h3 className="text-xl font-semibold">Ask the AI</h3>
      </div>

      <p className="mt-5 text-white/45">
        Ask why a question was recommended or request an explanation.
      </p>

      <button className="mt-6 text-violet-400 transition hover:text-violet-300">
        Open Chat →
      </button>
    </div>
  );
}
