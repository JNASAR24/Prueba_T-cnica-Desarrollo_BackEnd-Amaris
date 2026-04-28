import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/fundsApi";
import { useAuth } from "../auth/AuthContext";
import AppShell from "../components/AppShell";
import StatusMessage from "../components/StatusMessage";

export default function LoginPage() {
  const [documentNumber, setDocumentNumber] = useState("123456789");
  const [password, setPassword] = useState("123456");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const { setSession } = useAuth();
  const navigate = useNavigate();

  // Flujo de autenticación principal.
  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setMessage("");
    try {
      const response = await login(documentNumber, password);
      setSession(response.token);
      setMessage("Sesion iniciada correctamente.");
      navigate("/", { replace: true });
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <AppShell
      title="BTG Funds - Prueba Tecnica"
      subtitle="Autenticacion para consumir el microservicio de fondos"
    >
      <main className="panel auth-panel">
        <h2>Inicio de sesion</h2>
        <form onSubmit={handleSubmit}>
          <label>
            Documento
            <input value={documentNumber} onChange={(e) => setDocumentNumber(e.target.value)} />
          </label>
          <label>
            Password
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
          </label>
          <button type="submit" disabled={loading}>
            {loading ? "Ingresando..." : "Ingresar"}
          </button>
        </form>
      </main>
      <StatusMessage message={message} />
    </AppShell>
  );
}
