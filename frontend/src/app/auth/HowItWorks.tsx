import { BrainCircuit, LogIn, Puzzle } from "lucide-react";

const steps = [
  {
    icon: LogIn,
    title: "Sign in with Google",
    desc: "Secure authentication",
  },
  {
    icon: Puzzle,
    title: "Install Extension",
    desc: "One click setup",
  },
  {
    icon: BrainCircuit,
    title: "Receive AI Recommendations",
    desc: "Based on your attempts",
  },
];

export default function HowItWorks() {
  return (
    <div className="mt-4">
      <p className="mb-4 text-center text-xs uppercase tracking-[0.3em] text-white/20">
        What happens next
      </p>

      <div className="space-y-6">
        {steps.map((step, index) => {
          const Icon = step.icon;

          return (
            <div key={step.title} className="relative flex items-start gap-4">
              <div className="relative">
                <div className="flex h-10 w-10 items-center justify-center rounded-full border border-cyan-500/20 bg-cyan-500/10">
                  <Icon size={18} className="text-cyan-400" />
                </div>

                {index !== steps.length - 1 && (
                  <div className="absolute left-1/2 top-10 h-8 w-px -translate-x-1/2 bg-white/10" />
                )}
              </div>

              <div>
                <p className="text-sm font-medium text-white">{step.title}</p>

                <p className="mt-1 text-sm text-white/40">{step.desc}</p>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
