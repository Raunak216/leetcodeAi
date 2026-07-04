import api from "@/lib/api";

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
  const response = await api.post("/recommendations/company", body);

  return response.data;
}
