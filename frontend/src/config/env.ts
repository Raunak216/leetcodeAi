const useProduction =
  process.env.NEXT_PUBLIC_USE_PRODUCTION === "true" ||
  process.env.NODE_ENV === "production";

export const env = {
  isProd: useProduction,

  backendUrl: useProduction
    ? process.env.NEXT_PUBLIC_PROD_BACKEND || "https://api.unsheet.in"
    : process.env.NEXT_PUBLIC_LOCAL_BACKEND || "http://localhost:8080",

  frontendUrl: useProduction
    ? process.env.NEXT_PUBLIC_PROD_FRONTEND || "https://unsheet.in"
    : process.env.NEXT_PUBLIC_LOCAL_FRONTEND || "http://localhost:3000",

  EXTENSION_ID:
    process.env.NEXT_PUBLIC_EXTENSION_ID || "cnfpjafdoegkfnbhnejcjdbppddccppg",
};
