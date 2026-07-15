import axios from "axios";
import { env } from "@/config/env";

const api = axios.create({
  baseURL: env.backendUrl,
  withCredentials: true,
});

export default api;
