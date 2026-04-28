import { useAuth } from "../auth/AuthContext";

/**
 * Estructura visual principal de la app.
 */
export default function AppShell({ title, subtitle, children }) {
  const { isAuthenticated, clearSession } = useAuth();

  return (
    <div className="app-bg">
      <div className="layout">
        <header className="topbar">
          <div className="title-wrap">
            <p className="eyebrow">Prueba tecnica bancaria</p>
            <h1>{title}</h1>
            {subtitle && <p className="subtitle">{subtitle}</p>}
          </div>
          {isAuthenticated && (
            <button className="secondary-button" onClick={clearSession}>
              Cerrar sesion
            </button>
          )}
        </header>
        {children}
      </div>
    </div>
  );
}
