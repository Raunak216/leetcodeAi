import { TopicMastery } from "@/types/Dashboard";

interface Props {
  topics: TopicMastery[];
}

export default function FocusAreasCard({ topics }: Props) {
  return (
    <div className="rounded-3xl border border-red-500/10 bg-red-500/[0.03] p-6">
      <h3 className="mb-6 text-xl font-semibold">Focus Areas</h3>

      <div className="space-y-5">
        {topics.map((topic) => (
          <div key={topic.topic}>
            <div className="mb-2 flex justify-between">
              <p className="truncate pr-3 text-white/65">
                {topic.topic
                  .replaceAll("_", " ")
                  .replace(/\b\w/g, (c) => c.toUpperCase())}
              </p>

              <span className="text-sm text-red-400">
                {Math.round(topic.mastery)}%
              </span>
            </div>

            <div className="h-1.5 rounded-full bg-white/10">
              <div
                style={{
                  width: `${topic.mastery}%`,
                }}
                className="h-full rounded-full bg-red-400 transition-all duration-500"
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
