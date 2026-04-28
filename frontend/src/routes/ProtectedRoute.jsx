import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

/**
 * Guard de ruta:
 * si no hay sesión, redirige al login.
 */
export default function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  return children;
}
