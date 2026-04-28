import { createContext, useContext, useMemo, useState } from "react";

const AuthContext = createContext(null);

/**
 * Provider de autenticación.
 * Centraliza token y operaciones de login/logout para toda la app.
 */
export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token") ?? "");

  const value = useMemo(
    () => ({
      token,
      isAuthenticated: Boolean(token),
      setSession(newToken) {
        setToken(newToken);
        localStorage.setItem("token", newToken);
      },
      clearSession() {
        localStorage.removeItem("token");
        setToken("");
      }
    }),
    [token]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth debe usarse dentro de AuthProvider");
  }
  return context;
}
