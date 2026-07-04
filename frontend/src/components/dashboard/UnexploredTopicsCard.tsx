interface Props {
  topics: string[];
}

export default function UnexploredTopicsCard({ topics }: Props) {
  return (
    <div className="rounded-3xl border border-violet-500/20 bg-violet-500/[0.03] p-6">
      <h3 className="text-xl font-semibold">Unexplored Topics</h3>

      <p className="mt-3 text-sm text-white/40">
        Areas you haven't practiced yet.
      </p>

      <div className="mt-6 flex flex-wrap gap-2">
        {topics.map((topic) => (
          <span
            key={topic}
            className="rounded-full border border-violet-500/20 bg-violet-500/10 px-3 py-1 text-sm text-violet-300"
          >
            {topic
              .replaceAll("_", " ")
              .replace(/\b\w/g, (c) => c.toUpperCase())}
          </span>
        ))}
      </div>
    </div>
  );
}
