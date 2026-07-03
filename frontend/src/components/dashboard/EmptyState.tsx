import { Target } from "lucide-react";

export default function EmptyState() {
  return (
    <div className="flex h-[420px] items-start justify-center pt-28">
      <div className="text-center">
        <div className="mx-auto flex h-24 w-24 items-center justify-center rounded-full border border-cyan-500/20 bg-cyan-500/10">
          <Target size={34} className="text-cyan-400" />
        </div>

        <h2 className="mt-8 text-3xl font-bold">
          Your queue is ready to generate
        </h2>

        <p className="mt-5 max-w-md text-lg leading-8 text-white/35">
          Select your preparation strategy and let AlgoLens build the highest
          ROI roadmap for your interview.
        </p>
      </div>
    </div>
  );
}
