import AskAiCard from "./AskAiCard";
import FocusAreasCard from "./FocusAreasCard";
import TopSkillsCard from "./TopSkillsCard";

export default function RightSidebar() {
  return (
    <div className="w-[300px] shrink-0 space-y-5">
      <TopSkillsCard />

      <FocusAreasCard />

      <AskAiCard />
    </div>
  );
}
