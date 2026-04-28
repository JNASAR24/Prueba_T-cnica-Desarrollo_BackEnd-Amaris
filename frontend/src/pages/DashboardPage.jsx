import { useState } from "react";
import {
  cancelSubscription,
  createSubscription,
  getFunds,
  getTransactions
} from "../api/fundsApi";
import { useAuth } from "../auth/AuthContext";
import AppShell from "../components/AppShell";
import StatusMessage from "../components/StatusMessage";

export default function DashboardPage() {
  const { token } = useAuth();
  const [funds, setFunds] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [selectedFundId, setSelectedFundId] = useState("");
  const [notificationChannel, setNotificationChannel] = useState("EMAIL");
  const [subscriptionId, setSubscriptionId] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleLoadFunds() {
    setLoading(true);
    setMessage("");
    try {
      const response = await getFunds(token);
      setFunds(response);
      if (response.length > 0 && !selectedFundId) {
        setSelectedFundId(response[0].id);
      }
      setMessage("Fondos cargados.");
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleSubscribe() {
    if (!selectedFundId) {
      setMessage("Selecciona un fondo.");
      return;
    }

    setLoading(true);
    setMessage("");
    try {
      const response = await createSubscription(token, selectedFundId, notificationChannel);
      setSubscriptionId(response.id);
      setMessage(`Suscripcion creada: ${response.id}`);
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

    setLoading(true);
    setMessage("");
    try {
      const response = await cancelSubscription(token, subscriptionId, notificationChannel);
      setMessage(response.message ?? "Suscripcion cancelada.");
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
      setMessage("Historial cargado.");
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <AppShell title="BTG Funds Portal">
      <main className="grid">
        <section className="panel">
          <h2>Fondos</h2>
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
          <div className="actions">
            <button onClick={handleSubscribe} disabled={loading}>
              Suscribirse
            </button>
          </div>
        </section>

        <section className="panel">
          <h2>Cancelar suscripcion</h2>
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
              Cancelar
            </button>
          </div>
        </section>

        <section className="panel panel-full">
          <h2>Historial de transacciones</h2>
          <div className="actions">
            <button onClick={handleLoadTransactions} disabled={loading}>
              Consultar historial
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
                  <td>{tx.fundId}</td>
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
      </main>
      <StatusMessage message={message} />
    </AppShell>
  );
}
