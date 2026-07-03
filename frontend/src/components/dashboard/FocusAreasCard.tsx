const focus = ["Dynamic Programming", "Graphs", "Heap"];

export default function FocusAreasCard() {
  return (
    <div className="rounded-3xl border border-red-500/10 bg-red-500/[0.03] p-6">
      <h3 className="mb-5 text-xl font-semibold">Focus Areas</h3>

      <div className="space-y-3">
        {focus.map((item) => (
          <p key={item} className="text-white/60">
            • {item}
          </p>
        ))}
      </div>
    </div>
  );
}
