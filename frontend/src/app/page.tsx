import HeroSection from "@/app/auth/HeroSection";
import LoginCard from "@/app/auth/LoginCard";

export default function HomePage() {
  return (
    <main className="relative min-h-screen overflow-hidden bg-[#050608]">
      {/* Background Glow */}

      <div className="absolute left-0 top-0 h-[700px] w-[700px] rounded-full bg-cyan-500/10 blur-[180px]" />

      <div className="absolute bottom-0 right-0 h-[600px] w-[600px] rounded-full bg-violet-500/10 blur-[180px]" />

      <HeroSection />

      <div className="relative z-10 flex justify-center px-5 pb-20">
        <LoginCard />
      </div>
    </main>
  );
}
