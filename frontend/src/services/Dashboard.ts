import api from "@/lib/api";
import { Dashboard } from "@/types/Dashboard";

export async function getDashboard() {
  const response = await api.get<Dashboard>("/dashboard");

  return response.data;
}
