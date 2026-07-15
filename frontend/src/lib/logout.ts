import api from "./api";
export default async function logout() {
  await api.post("/auth/logout");
  const EXTENSION_ID = process.env.NEXT_PUBLIC_EXTENSION_ID!;

  if (window.chrome?.runtime) {
    window.chrome.runtime.sendMessage(EXTENSION_ID, {
      type: "SET_AUTH_TOKEN",
      token: null,
    });
  }
  window.location.href = "/";
}
