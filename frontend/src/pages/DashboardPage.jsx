import { useMemo, useState } from "react";
import {
  cancelSubscription,
  createSubscription,
  getFunds,
  getTransactions
} from "../api/fundsApi";
import { useAuth } from "../auth/AuthContext";
import AppShell from "../components/AppShell";
import StatusMessage from "../components/StatusMessage";

const FLOW_STEPS = [
  "1. Inicia sesion",
  "2. Carga fondos",
  "3. Crea suscripcion",
  "4. Consulta historial",
  "5. Cancela suscripcion"
];

function getFundName(funds, fundId) {
  const fund = funds.find((item) => String(item.id) === String(fundId));
  return fund?.name ?? fundId;
}

export default function DashboardPage() {
  const { token } = useAuth();
  const [funds, setFunds] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [selectedFundId, setSelectedFundId] = useState("");
  const [notificationChannel, setNotificationChannel] = useState("EMAIL");
  const [email, setEmail] = useState("demo@btg.com");
  const [phoneNumber, setPhoneNumber] = useState("+573001112233");
  const [subscriptionId, setSubscriptionId] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const activeSubscriptions = useMemo(
    () => transactions.filter((tx) => tx.type === "SUBSCRIPTION").length,
    [transactions]
  );
  const cancellationCount = useMemo(
    () => transactions.filter((tx) => tx.type === "CANCELLATION").length,
    [transactions]
  );

  async function handleLoadFunds() {
    setLoading(true);
    setMessage("");
    try {
      const response = await getFunds(token);
      setFunds(response);
      if (response.length > 0 && !selectedFundId) {
        setSelectedFundId(String(response[0].id));
      }
      setMessage("Fondos cargados correctamente.");
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleSubscribe() {
    if (!selectedFundId) {
      setMessage("Selecciona un fondo para continuar.");
      return;
    }

    const payload = {
      fundId: Number(selectedFundId),
      notificationChannel,
      email: notificationChannel === "EMAIL" ? email : undefined,
      phoneNumber: notificationChannel === "SMS" ? phoneNumber : undefined
    };

    setLoading(true);
    setMessage("");
    try {
      const response = await createSubscription(token, payload);
      setSubscriptionId(response.id);
      setMessage(`Suscripcion creada con exito: ${response.id}`);
      await handleLoadTransactions();
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleCancelSubscription() {
    if (!subscriptionId) {
      setMessage("Ingresa un subscriptionId para cancelar.");
      return;
    }

    const payload = {
      notificationChannel,
      email: notificationChannel === "EMAIL" ? email : undefined,
      phoneNumber: notificationChannel === "SMS" ? phoneNumber : undefined
    };

    setLoading(true);
    setMessage("");
    try {
      const response = await cancelSubscription(token, subscriptionId, payload);
      setMessage(response.message ?? "Suscripcion cancelada con exito.");
      await handleLoadTransactions();
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleLoadTransactions() {
    setLoading(true);
    setMessage("");
    try {
      const response = await getTransactions(token);
      setTransactions(response);
      setMessage("Historial de transacciones cargado.");
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <AppShell
      title="BTG Funds - Prueba Tecnica"
      subtitle="Frontend de validacion funcional para flujos locales y AWS"
    >
      <main className="workspace">
        <aside className="side-panel fade-up">
          <h2>Flujo recomendado</h2>
          <ul className="step-list">
            {FLOW_STEPS.map((step) => (
              <li key={step}>{step}</li>
            ))}
          </ul>
          <div className="env-card">
            <p className="env-title">Entorno activo</p>
            <p className="env-value">{import.meta.env.VITE_API_BASE_URL}</p>
          </div>
        </aside>

        <section className="content-panel">
          <section className="kpi-grid fade-up">
            <article className="kpi-card">
              <p>Total fondos</p>
              <strong>{funds.length}</strong>
            </article>
            <article className="kpi-card">
              <p>Suscripciones</p>
              <strong>{activeSubscriptions}</strong>
            </article>
            <article className="kpi-card">
              <p>Cancelaciones</p>
              <strong>{cancellationCount}</strong>
            </article>
            <article className="kpi-card">
              <p>Transacciones</p>
              <strong>{transactions.length}</strong>
            </article>
          </section>

          <section className="module-grid">
            <article className="panel fade-up">
              <h3>1) Fondos y canal</h3>
              <p className="helper-text">
                Carga fondos disponibles, selecciona uno y define el canal de notificacion.
              </p>
              <div className="actions">
                <button onClick={handleLoadFunds} disabled={loading}>
                  Cargar fondos
                </button>
              </div>
              <label>
                Fondo
                <select value={selectedFundId} onChange={(e) => setSelectedFundId(e.target.value)}>
                  <option value="">Seleccionar...</option>
                  {funds.map((fund) => (
                    <option key={fund.id} value={fund.id}>
                      {fund.name} - Min: {fund.minimumAmount}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Canal de notificacion
                <select value={notificationChannel} onChange={(e) => setNotificationChannel(e.target.value)}>
                  <option value="EMAIL">EMAIL</option>
                  <option value="SMS">SMS</option>
                </select>
              </label>
              {notificationChannel === "EMAIL" ? (
                <label>
                  Correo destino
                  <input
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="correo@dominio.com"
                  />
                </label>
              ) : (
                <label>
                  Celular destino
                  <input
                    value={phoneNumber}
                    onChange={(e) => setPhoneNumber(e.target.value)}
                    placeholder="+573001112233"
                  />
                </label>
              )}
              <div className="actions">
                <button onClick={handleSubscribe} disabled={loading}>
                  Crear suscripcion
                </button>
              </div>
            </article>

            <article className="panel fade-up">
              <h3>2) Cancelar suscripcion</h3>
              <p className="helper-text">
                Usa el subscriptionId generado para ejecutar la cancelacion y devolver saldo.
              </p>
              <label>
                Subscription ID
                <input
                  placeholder="UUID de suscripcion"
                  value={subscriptionId}
                  onChange={(e) => setSubscriptionId(e.target.value)}
                />
              </label>
              <div className="actions">
                <button onClick={handleCancelSubscription} disabled={loading}>
                  Cancelar suscripcion
                </button>
              </div>
            </article>
          </section>

          <section className="panel panel-full fade-up">
            <div className="history-header">
              <div>
                <h3>3) Historial de transacciones</h3>
                <p className="helper-text">Consulta y evidencia de consumos del microservicio.</p>
              </div>
              <button onClick={handleLoadTransactions} disabled={loading}>
                Actualizar historial
              </button>
            </div>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Tipo</th>
                  <th>Fondo</th>
                  <th>Monto</th>
                  <th>Fecha</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map((tx) => (
                  <tr key={tx.id}>
                    <td>{tx.id}</td>
                    <td>{tx.type}</td>
                    <td>{getFundName(funds, tx.fundId)}</td>
                    <td>{tx.amount}</td>
                    <td>{tx.createdAt}</td>
                  </tr>
                ))}
                {transactions.length === 0 && (
                  <tr>
                    <td colSpan="5">Sin transacciones para mostrar.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </section>
        </section>
      </main>
      <StatusMessage message={message} />
    </AppShell>
  );
}
