"use client";

import api from "@/lib/api";
import { User } from "@/types/User";
import { createContext, useContext, useEffect, useState } from "react";
import { env } from "@/config/env";

interface AuthContextType {
  user: User | null;
  loading: boolean;
  refreshUser: () => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  // Helper to push extension token whenever user is authenticated
  const syncExtensionToken = async () => {
    try {
      const EXTENSION_ID = env.EXTENSION_ID;
      if (!EXTENSION_ID || !window.chrome?.runtime) return;

      // Fetch fresh token for extension
      const { data } = await api.get("/auth/extension-token");

      if (data?.token) {
        window.chrome.runtime.sendMessage(EXTENSION_ID, {
          type: "SET_AUTH_TOKEN",
          token: data.token,
        });
      }
    } catch (err) {
      console.error("Extension token sync error:", err);
    }
  };

  const refreshUser = async () => {
    try {
      const response = await api.get("/auth/me");
      setUser(response.data);
      // Sync with Chrome Extension after successfully fetching user
      await syncExtensionToken();
    } catch (error: any) {
      if (error.response?.status !== 401) {
        console.error(error);
      }
      setUser(null);
    }
  };

  const logout = async () => {
    try {
      await api.post("/auth/logout");

      // Notify Extension of Logout
      const EXTENSION_ID = env.EXTENSION_ID;
      if (EXTENSION_ID && window.chrome?.runtime) {
        window.chrome.runtime.sendMessage(EXTENSION_ID, {
          type: "LOGOUT",
        });
      }
    } finally {
      setUser(null);
      window.location.href = "/";
    }
  };

  useEffect(() => {
    const initialize = async () => {
      await refreshUser();
      setLoading(false);
    };

    initialize();
  }, []);

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        refreshUser,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }

  return context;
}
