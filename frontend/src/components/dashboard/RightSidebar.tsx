"use client";

import { useEffect, useState } from "react";

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
    return <div className="w-[300px] shrink-0">Loading...</div>;
  }

  return (
    <div className="w-[300px] shrink-0 space-y-5">
      <TopSkillsCard skills={dashboard.strongTopics} />

      <FocusAreasCard topics={dashboard.weakTopics} />

      <UnexploredTopicsCard topics={dashboard.unexploredTopics} />
    </div>
  );
}
