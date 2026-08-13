"use client";

import { useEffect, useState, useMemo } from "react";
import { getDashboard } from "@/services/Dashboard";
import { Dashboard, TopicMastery } from "@/types/Dashboard";
import {
  Award,
  Target,
  Flame,
  Compass,
  ArrowLeft,
  Search,
  CheckCircle2,
  AlertCircle,
  Clock,
  Sparkles,
} from "lucide-react";
import Link from "next/link";

export default function SkillsPage() {
  const [dashboard, setDashboard] = useState<Dashboard | null>(null);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  const [activeTab, setActiveTab] = useState<
    "all" | "mastered" | "in_progress" | "focus" | "unexplored"
  >("all");

  useEffect(() => {
    getDashboard()
      .then(setDashboard)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const formatTopic = (topic: string) =>
    topic.replaceAll("_", " ").replace(/\b\w/g, (c) => c.toUpperCase());

  // Normalize and categorize skills
  const { allSkills, mastered, inProgress, focusNeeded, unexplored } =
    useMemo(() => {
      if (!dashboard)
        return {
          allSkills: [],
          mastered: [],
          inProgress: [],
          focusNeeded: [],
          unexplored: [],
        };

      const combined: (TopicMastery & { status: string })[] = [
        ...dashboard.strongTopics.map((s) => ({
          ...s,
          status:
            s.mastery >= 75
              ? "mastered"
              : s.mastery >= 40
                ? "in_progress"
                : "focus",
        })),
        ...dashboard.weakTopics.map((s) => ({
          ...s,
          status:
            s.mastery >= 75
              ? "mastered"
              : s.mastery >= 40
                ? "in_progress"
                : "focus",
        })),
      ];

      const uniqueSkills = Array.from(
        new Map(combined.map((item) => [item.topic, item])).values(),
      );

      const m = uniqueSkills.filter((s) => s.mastery >= 75);
      const ip = uniqueSkills.filter((s) => s.mastery >= 40 && s.mastery < 75);
      const f = uniqueSkills.filter((s) => s.mastery < 40);
      const u = dashboard.unexploredTopics || [];

      return {
        allSkills: uniqueSkills,
        mastered: m,
        inProgress: ip,
        focusNeeded: f,
        unexplored: u,
      };
    }, [dashboard]);

  // Search Filter
  const filteredSkills = useMemo(() => {
    return allSkills.filter((skill) =>
      skill.topic.toLowerCase().includes(searchQuery.toLowerCase()),
    );
  }, [allSkills, searchQuery]);

  const filteredUnexplored = useMemo(() => {
    return unexplored.filter((topic) =>
      topic.toLowerCase().includes(searchQuery.toLowerCase()),
    );
  }, [unexplored, searchQuery]);

  if (loading) {
    return (
      <div className="flex h-96 items-center justify-center text-white/40 text-sm font-medium">
        Loading 100+ skills profile...
      </div>
    );
  }

  if (!dashboard) return null;

  return (
    <div className="mx-auto max-w-7xl p-8 text-white">
      {/* Header */}
      <div className="mb-8">
        <Link
          href="/dashboard"
          className="mb-3 inline-flex items-center gap-2 text-sm text-white/40 hover:text-white transition"
        >
          <ArrowLeft size={16} /> Back to Dashboard
        </Link>
        <h1 className="text-3xl font-bold tracking-tight">
          Topic Mastery & Skills
        </h1>
        <p className="mt-1 text-sm text-white/40">
          Tracking your complete algorithmic proficiency across all{" "}
          {allSkills.length + unexplored.length} topics.
        </p>
      </div>

      {/* Top Stat Summary Cards */}
      <div className="mb-8 grid grid-cols-2 gap-4 sm:grid-cols-4">
        <div className="rounded-2xl border border-cyan-500/20 bg-cyan-500/[0.03] p-4 sm:p-5">
          <div className="flex items-center justify-between text-cyan-400">
            <span className="text-xs font-semibold uppercase tracking-wider text-white/40">
              Mastered (&ge; 75%)
            </span>
            <Award size={18} />
          </div>
          <p className="mt-2 text-2xl sm:text-3xl font-bold text-cyan-400">
            {mastered.length}
          </p>
        </div>

        <div className="rounded-2xl border border-amber-500/20 bg-amber-500/[0.03] p-4 sm:p-5">
          <div className="flex items-center justify-between text-amber-400">
            <span className="text-xs font-semibold uppercase tracking-wider text-white/40">
              In Progress (40-74%)
            </span>
            <Flame size={18} />
          </div>
          <p className="mt-2 text-2xl sm:text-3xl font-bold text-amber-400">
            {inProgress.length}
          </p>
        </div>

        <div className="rounded-2xl border border-rose-500/20 bg-rose-500/[0.03] p-4 sm:p-5">
          <div className="flex items-center justify-between text-rose-400">
            <span className="text-xs font-semibold uppercase tracking-wider text-white/40">
              Needs Focus (&lt; 40%)
            </span>
            <Target size={18} />
          </div>
          <p className="mt-2 text-2xl sm:text-3xl font-bold text-rose-400">
            {focusNeeded.length}
          </p>
        </div>

        <div className="rounded-2xl border border-violet-500/20 bg-violet-500/[0.03] p-4 sm:p-5">
          <div className="flex items-center justify-between text-violet-400">
            <span className="text-xs font-semibold uppercase tracking-wider text-white/40">
              Unexplored
            </span>
            <Compass size={18} />
          </div>
          <p className="mt-2 text-2xl sm:text-3xl font-bold text-violet-400">
            {unexplored.length}
          </p>
        </div>
      </div>

      {/* Search and Filter Controls */}
      <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        {/* Search Bar */}
        <div className="relative flex-1 max-w-md">
          <Search
            size={18}
            className="absolute left-4 top-1/2 -translate-y-1/2 text-white/30"
          />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search topics (e.g. Graph, DP, Tree)..."
            className="w-full rounded-2xl border border-white/10 bg-[#0B0D11] py-2.5 pl-11 pr-4 text-sm outline-none placeholder:text-white/30 focus:border-cyan-500/50 transition"
          />
        </div>

        {/* Filter Tabs */}
        <div className="flex flex-wrap gap-1.5 rounded-2xl border border-white/10 bg-[#0B0D11] p-1.5">
          {[
            {
              id: "all",
              label: `All (${allSkills.length + unexplored.length})`,
            },
            { id: "mastered", label: `Mastered (${mastered.length})` },
            { id: "in_progress", label: `In Progress (${inProgress.length})` },
            { id: "focus", label: `Focus (${focusNeeded.length})` },
            { id: "unexplored", label: `Unexplored (${unexplored.length})` },
          ].map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id as any)}
              className={`rounded-xl px-3 py-1.5 text-xs font-semibold transition ${
                activeTab === tab.id
                  ? "bg-white/10 text-white"
                  : "text-white/40 hover:text-white/70"
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>
      </div>

      {/* Main Matrix Grid View */}
      <div className="space-y-8">
        {/* Mastered Section */}
        {(activeTab === "all" || activeTab === "mastered") && (
          <CompactSkillSection
            title="Mastered Topics"
            count={mastered.length}
            badgeColor="cyan"
            icon={<CheckCircle2 size={18} className="text-cyan-400" />}
            skills={filteredSkills.filter((s) => s.mastery >= 75)}
            formatTopic={formatTopic}
          />
        )}

        {/* In Progress Section */}
        {(activeTab === "all" || activeTab === "in_progress") && (
          <CompactSkillSection
            title="In Progress"
            count={inProgress.length}
            badgeColor="amber"
            icon={<Clock size={18} className="text-amber-400" />}
            skills={filteredSkills.filter(
              (s) => s.mastery >= 40 && s.mastery < 75,
            )}
            formatTopic={formatTopic}
          />
        )}

        {/* Needs Focus Section */}
        {(activeTab === "all" || activeTab === "focus") && (
          <CompactSkillSection
            title="Needs Focus"
            count={focusNeeded.length}
            badgeColor="rose"
            icon={<AlertCircle size={18} className="text-rose-400" />}
            skills={filteredSkills.filter((s) => s.mastery < 40)}
            formatTopic={formatTopic}
          />
        )}

        {/* Unexplored Topics Section */}
        {(activeTab === "all" || activeTab === "unexplored") &&
          filteredUnexplored.length > 0 && (
            <section className="rounded-3xl border border-violet-500/20 bg-violet-500/[0.02] p-6">
              <div className="mb-5 flex items-center justify-between">
                <div className="flex items-center gap-2.5">
                  <Compass size={18} className="text-violet-400" />
                  <h2 className="text-lg font-semibold text-white">
                    Unexplored Topics
                  </h2>
                </div>
                <span className="rounded-full bg-violet-500/10 px-3 py-1 text-xs font-semibold text-violet-300 border border-violet-500/20">
                  {filteredUnexplored.length} topics
                </span>
              </div>
              <div className="flex flex-wrap gap-2">
                {filteredUnexplored.map((topic) => (
                  <span
                    key={topic}
                    className="rounded-xl border border-violet-500/20 bg-violet-500/10 px-3 py-1.5 text-xs font-medium text-violet-300 transition hover:border-violet-500/40"
                  >
                    {formatTopic(topic)}
                  </span>
                ))}
              </div>
            </section>
          )}
      </div>
    </div>
  );
}

// -------------------------------------------------------------
// Sleek Compact Skill Matrix Card Component (4 - 5 items per row)
// -------------------------------------------------------------
function CompactSkillSection({
  title,
  count,
  badgeColor,
  icon,
  skills,
  formatTopic,
}: {
  title: string;
  count: number;
  badgeColor: "cyan" | "amber" | "rose";
  icon: React.ReactNode;
  skills: TopicMastery[];
  formatTopic: (t: string) => string;
}) {
  if (skills.length === 0) return null;

  const styles = {
    cyan: {
      border: "border-cyan-500/20",
      bg: "bg-cyan-500/[0.02]",
      badge: "border-cyan-500/30 bg-cyan-500/10 text-cyan-400",
      cardHover: "hover:border-cyan-500/40",
    },
    amber: {
      border: "border-amber-500/20",
      bg: "bg-amber-500/[0.02]",
      badge: "border-amber-500/30 bg-amber-500/10 text-amber-400",
      cardHover: "hover:border-amber-500/40",
    },
    rose: {
      border: "border-rose-500/20",
      bg: "bg-rose-500/[0.02]",
      badge: "border-rose-500/30 bg-rose-500/10 text-rose-400",
      cardHover: "hover:border-rose-500/40",
    },
  }[badgeColor];

  return (
    <section className={`rounded-3xl border ${styles.border} ${styles.bg} p-6`}>
      <div className="mb-5 flex items-center justify-between">
        <div className="flex items-center gap-2.5">
          {icon}
          <h2 className="text-lg font-semibold text-white">{title}</h2>
        </div>
        <span
          className={`rounded-full border px-3 py-1 text-xs font-semibold ${styles.badge}`}
        >
          {count} items
        </span>
      </div>

      {/* Grid: 2 cols on mobile, 3 on md, 4 on lg, 5 on xl */}
      <div className="grid grid-cols-2 gap-2.5 sm:grid-cols-3 md:grid-cols-4 xl:grid-cols-5">
        {skills.map((skill) => (
          <div
            key={skill.topic}
            className={`flex items-center justify-between gap-2 rounded-xl border border-white/10 bg-[#0B0D11] px-3.5 py-2.5 transition ${styles.cardHover}`}
          >
            <span
              className="truncate text-xs font-medium text-white/80"
              title={formatTopic(skill.topic)}
            >
              {formatTopic(skill.topic)}
            </span>

            {/* Glowing Compact Percentage Badge */}
            <span
              className={`shrink-0 rounded-lg border px-2 py-0.5 text-[11px] font-bold ${styles.badge}`}
            >
              {Math.round(skill.mastery)}%
            </span>
          </div>
        ))}
      </div>
    </section>
  );
}
