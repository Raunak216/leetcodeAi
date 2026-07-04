import api from "@/lib/api";

export async function getCompanies() {
  const response = await api.get("/companies");

  return response.data;
}
