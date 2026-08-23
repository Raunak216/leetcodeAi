import api from "@/lib/api";

import { AiInsightsResponse, QuestionAttempt } from "@/types/Attempt";

export async function getMyAttempts(): Promise<QuestionAttempt[]> {
  const response = await api.get<QuestionAttempt[]>("/attempts/me");

  return response.data;
}

export async function getAttempt(attemptId: number): Promise<QuestionAttempt> {
  const response = await api.get<QuestionAttempt>(`/attempts/${attemptId}`);

  return response.data;
}

export async function generateAttemptInsights(
  attemptId: number,
): Promise<AiInsightsResponse> {
  const response = await api.post<AiInsightsResponse>(
    `/attempts/${attemptId}/insights`,
  );

  return response.data;
}
