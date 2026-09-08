import { createContext, useContext, useMemo, useState } from "react";
import { apiRequest } from "../services/api";

const STORAGE_KEY = "iceibank.session";
const AuthContext = createContext(null);

function readSession() {
  try {
    return JSON.parse(localStorage.getItem(STORAGE_KEY));
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [session, setSession] = useState(readSession);

  async function login(agency, username, password) {
    const response = await apiRequest(agency, "/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ username, password })
    });
    const nextSession = { ...agency, token: response.token, expiresIn: response.expiresIn };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(nextSession));
    setSession(nextSession);
  }

  function logout() {
    localStorage.removeItem(STORAGE_KEY);
    setSession(null);
  }

  const value = useMemo(() => ({ session, login, logout }), [session]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
