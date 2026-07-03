import { Zap } from "lucide-react";

export default function Logo() {
  return (
    <div className="flex items-center gap-3">
      <div className="flex h-11 w-11 items-center justify-center rounded-xl border border-cyan-500/20 bg-cyan-500/10">
        <Zap className="h-5 w-5 text-cyan-400" />
      </div>

      <h1 className="text-3xl font-bold tracking-tight text-white">
        Algo<span className="text-cyan-400">Lens</span>
      </h1>
    </div>
  );
}
