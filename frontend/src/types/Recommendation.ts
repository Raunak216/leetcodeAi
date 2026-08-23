export interface RecommendedQuestion {
  title: string;
  slug: string;
  difficulty: "Easy" | "Medium" | "Hard";
  topics: string[];
  reason: string;
  estimatedTime: string;
}

export interface RecommendationResponse {
  questions: RecommendedQuestion[];
}

export interface SavedRecommendation {
  recommendation: RecommendationResponse;
  mode: "general" | "company";
  company: string;
  interviewScheduled: boolean;
  daysRemaining: number | null;
}
