import { request } from "./httpClient";

export function login(documentNumber, password) {
  return request("/api/v1/auth/login", {
    method: "POST",
    body: { documentNumber, password }
  });
}

export function getFunds(token) {
  return request("/api/v1/funds", { token });
}

export function createSubscription(token, payload) {
  return request("/api/v1/subscriptions", {
    method: "POST",
    token,
    body: payload
  });
}

export function cancelSubscription(token, subscriptionId, payload) {
  return request(`/api/v1/subscriptions/${subscriptionId}`, {
    method: "DELETE",
    token,
    body: payload
  });
}

export function getTransactions(token) {
  return request("/api/v1/transactions", { token });
}
