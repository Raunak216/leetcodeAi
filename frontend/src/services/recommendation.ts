import api from "@/lib/api";
import { RecommendationResponse } from "@/types/Recommendation";

export async function generateGeneralRecommendation(body: {
  interviewScheduled: boolean;
  daysRemaining: number | null;
}) {
  const response = await api.post("/recommendations/general", body);

  return response.data;
}
export async function generateCompanyRecommendation(body: {
  company: string;
  daysRemaining: number;
}) {
  const response = await api.post("/recommendations/company", {
    company: body.company,
    daysRemaining: Number(body.daysRemaining) || 0,
  });

  return response.data as RecommendationResponse;
}
