"use client";

import { useEffect, useState } from "react";
import Link from "next/link";

import TopSkillsCard from "./TopSkillsCard";
import FocusAreasCard from "./FocusAreasCard";
import UnexploredTopicsCard from "./UnexploredTopicsCard";

import { Dashboard } from "@/types/Dashboard";
import { getDashboard } from "@/services/Dashboard";

export default function RightSidebar() {
  const [dashboard, setDashboard] = useState<Dashboard | null>(null);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const data = await getDashboard();
        setDashboard(data);
      } catch (e) {
        console.error(e);
      }
    };

    fetchDashboard();
  }, []);

  if (!dashboard) {
    return (
      <div className="w-[300px] shrink-0 text-white/30 text-sm">Loading...</div>
    );
  }

  return (
    <div className="w-[300px] shrink-0 space-y-5">
      <div className="flex items-center justify-between px-1">
        <span className="text-xs uppercase tracking-widest text-white/30 font-semibold">
          Skills Overview
        </span>
        <Link
          href="/skills"
          className="text-xs font-medium text-cyan-400 hover:underline"
        >
          View All (
          {dashboard.strongTopics.length + dashboard.unexploredTopics.length})
          &rarr;
        </Link>
      </div>

      <TopSkillsCard skills={dashboard.strongTopics.slice(0, 5)} />

      <FocusAreasCard topics={dashboard.weakTopics.slice(0, 5)} />

      <UnexploredTopicsCard topics={dashboard.unexploredTopics.slice(0, 5)} />
    </div>
  );
}
