"use client";

import { Brain, LogOut, Settings, User } from "lucide-react";

export default function Sidebar() {
  return (
    <aside className="flex w-64 flex-col border-r border-white/10">
      <div className="flex items-center gap-3 border-b border-white/10 p-5">
        <div className="flex h-11 w-11 items-center justify-center rounded-xl border border-cyan-500/20 bg-cyan-500/10">
          ⚡
        </div>

        <h1 className="text-3xl font-bold">
          Algo<span className="text-cyan-400">Lens</span>
        </h1>
      </div>

      <div className="px-4 pt-6">
        <button className="mb-3 flex w-full items-center gap-3 rounded-2xl border border-cyan-500/20 bg-cyan-500/10 px-5 py-4 text-cyan-400">
          <Brain size={18} />
          Recommendations
        </button>

        <button className="flex w-full items-center gap-3 rounded-2xl px-5 py-4 text-white/60 transition hover:bg-white/5">
          <Brain size={18} />
          AI Assistant
        </button>
      </div>

      <div className="mt-auto border-t border-white/10 p-4">
        <button className="mb-2 flex w-full items-center gap-3 rounded-xl px-3 py-3 text-white/60 transition hover:bg-white/5">
          <Settings size={18} />
          Settings
        </button>

        <button className="flex w-full items-center gap-3 rounded-xl px-3 py-3 text-white/60 transition hover:bg-white/5">
          <LogOut size={18} />
          Sign Out
        </button>

        <div className="mt-6 flex items-center gap-3 rounded-xl border border-white/10 p-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-full bg-violet-500/10">
            <User size={18} />
          </div>

          <div>
            <p className="font-medium">Raunak Kumar</p>

            <p className="text-sm text-white/35">120+ topics tracked</p>
          </div>
        </div>
      </div>
    </aside>
  );
}
