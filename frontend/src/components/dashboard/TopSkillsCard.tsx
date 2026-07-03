const skills = [
  { name: "Arrays & Hashing", width: "90%" },
  { name: "Binary Search", width: "85%" },
  { name: "Two Pointers", width: "82%" },
  { name: "Sliding Window", width: "74%" },
  { name: "Stack", width: "70%" },
];

export default function TopSkillsCard() {
  return (
    <div className="rounded-3xl border border-white/10 bg-[#0B0D11] p-6">
      <div className="mb-7 flex items-center justify-between">
        <h3 className="text-2xl font-semibold">Top Skills</h3>

        <span className="text-sm text-white/25">120+</span>
      </div>

      <div className="space-y-5">
        {skills.map((skill) => (
          <div key={skill.name}>
            <div className="mb-2 flex justify-between">
              <p className="text-white/65">{skill.name}</p>

              <div className="h-2 w-2 rounded-full bg-cyan-400" />
            </div>

            <div className="h-1 rounded-full bg-white/10">
              <div
                style={{ width: skill.width }}
                className="h-full rounded-full bg-cyan-400"
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
