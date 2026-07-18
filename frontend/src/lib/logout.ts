import api from "./api";

export default async function logout() {
  await api.post("/auth/logout");

  window.location.href = "/";
}
