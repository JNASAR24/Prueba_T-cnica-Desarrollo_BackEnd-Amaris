import { useAuth } from "../auth/AuthContext";

/**
 * Estructura visual principal de la app.
 */
export default function AppShell({ title, children }) {
  const { isAuthenticated, clearSession } = useAuth();

  return (
    <div className="layout">
      <header className="topbar">
        <h1>{title}</h1>
        {isAuthenticated && <button onClick={clearSession}>Cerrar sesion</button>}
      </header>
      {children}
    </div>
  );
}
