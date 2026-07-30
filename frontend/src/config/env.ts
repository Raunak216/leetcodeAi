const useProduction = process.env.NEXT_PUBLIC_USE_PRODUCTION === "true";
export const env = {
  isProd: useProduction,

  backendUrl: useProduction
    ? process.env.NEXT_PUBLIC_PROD_BACKEND!
    : process.env.NEXT_PUBLIC_LOCAL_BACKEND!,

  frontendUrl: useProduction
    ? process.env.NEXT_PUBLIC_PROD_FRONTEND!
    : process.env.NEXT_PUBLIC_LOCAL_FRONTEND!,
  EXTENSION_ID: process.env.NEXT_PUBLIC_EXTENSION_ID!,
};
