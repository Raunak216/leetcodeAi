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
