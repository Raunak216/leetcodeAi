"use client";

import { Brain, History, LogOut, User } from "lucide-react";

import Link from "next/link";
import { usePathname } from "next/navigation";

import { useAuth } from "@/context/AuthContext";

export default function Sidebar() {
  const { logout, user } = useAuth();
  const pathname = usePathname();

  const isDashboard = pathname === "/dashboard";

  const isAttempts = pathname.startsWith("/attempts");

  return (
    <aside className="sticky top-0 flex h-screen w-64 flex-col border-r border-white/10 bg-[#050608]">
      {/* Logo */}
      <div className="flex items-center gap-3 border-b border-white/10 px-5 py-4">
        <img
          src="/unsheetLogo.svg"
          alt="unSheet Logo"
          className="h-18 w-auto object-contain"
        />
      </div>

      {/* Navigation */}
      <div className="flex-1 space-y-1.5 px-3 pt-4">
        <Link
          href="/dashboard"
          className={`flex w-full items-center gap-3 rounded-xl px-4 py-2.5 text-sm font-medium transition ${
            isDashboard
              ? "border border-cyan-500/20 bg-cyan-500/10 text-cyan-400"
              : "text-white/60 hover:bg-white/5 hover:text-white"
          }`}
        >
          <Brain size={18} />
          Recommendations
        </Link>

        <Link
          href="/attempts"
          className={`flex w-full items-center gap-3 rounded-xl px-4 py-2.5 text-sm font-medium transition ${
            isAttempts
              ? "border border-cyan-500/20 bg-cyan-500/10 text-cyan-400"
              : "text-white/60 hover:bg-white/5 hover:text-white"
          }`}
        >
          <History size={18} />
          My Attempts
        </Link>
      </div>

      {/* User */}
      <div className="border-t border-white/10 p-3">
        <button
          onClick={logout}
          className="flex w-full items-center gap-3 rounded-lg px-3 py-2 text-sm text-white/60 transition hover:bg-white/5 hover:text-red-400"
        >
          <LogOut size={18} />
          Sign Out
        </button>

        <div className="mt-3 flex items-center gap-3 rounded-xl border border-white/10 bg-white/[0.02] p-2.5">
          <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-violet-500/10 text-violet-400">
            <User size={18} />
          </div>

          <div className="min-w-0 flex-1">
            <p className="truncate text-sm font-medium text-white">
              {user?.userName || "Raunak Kumar"}
            </p>
          </div>
        </div>
      </div>
    </aside>
  );
}
