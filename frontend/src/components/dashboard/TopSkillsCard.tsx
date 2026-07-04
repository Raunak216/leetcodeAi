import { TopicMastery } from "@/types/Dashboard";

interface Props {
  skills: TopicMastery[];
}

export default function TopSkillsCard({ skills }: Props) {
  return (
    <div className="rounded-3xl border border-white/10 bg-[#0B0D11] p-6">
      <div className="mb-7 flex items-center justify-between">
        <h3 className="text-2xl font-semibold">Top Skills</h3>

        <span className="text-sm text-white/25">{skills.length}</span>
      </div>

      <div className="space-y-5">
        {skills.map((skill) => (
          <div key={skill.topic}>
            <div className="mb-2 flex justify-between">
              <p className="truncate pr-3 text-white/65">
                {skill.topic
                  .replaceAll("_", " ")
                  .replace(/\b\w/g, (c) => c.toUpperCase())}
              </p>

              <span className="text-sm text-cyan-400">
                {Math.round(skill.mastery)}%
              </span>
            </div>

            <div className="h-1.5 rounded-full bg-white/10">
              <div
                className="h-full rounded-full bg-cyan-400 transition-all duration-500"
                style={{
                  width: `${skill.mastery}%`,
                }}
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
