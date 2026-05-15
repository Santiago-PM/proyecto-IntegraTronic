import React, { createContext, useContext, useMemo, useState } from "react";
import { authApi } from "../services/api";
import { ApiError } from "../services/apiClient";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [authError, setAuthError] = useState(null);
  const [loading, setLoading] = useState(false);

  const value = useMemo(
    () => ({
      user,
      authError,
      loading,
      signIn: async (email, password) => {
        setLoading(true);
        setAuthError(null);
        try {
          const logged = await authApi.login(email, password);
          setUser({
            id: logged.idUsuario,
            name: logged.nombre,
            email: logged.email,
            rol: logged.nombreRol,
          });
          return true;
        } catch (err) {
          const message =
            err instanceof ApiError && err.status === 401
              ? "Usuario o contraseña incorrectos"
              : err?.message || "No se pudo conectar con el servidor";
          setAuthError(message);
          return false;
        } finally {
          setLoading(false);
        }
      },
      signOut: () => {
        setUser(null);
        setAuthError(null);
      },
    }),
    [user, authError, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}
