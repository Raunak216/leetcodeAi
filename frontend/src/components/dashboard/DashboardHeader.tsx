import { User } from "lucide-react";

export default function DashboardHeader() {
  return (
    <header className="flex items-center justify-between border-b border-white/10 px-8 py-5">
      <div>
        <h1 className="text-3xl font-bold">Recommended for You</h1>

        <p className="mt-1 text-white/30">
          Personalized from your coding history
        </p>
      </div>

      <button className="flex h-12 w-12 items-center justify-center rounded-full border border-violet-500/30 bg-violet-500/10">
        <User size={18} />
      </button>
    </header>
  );
}
