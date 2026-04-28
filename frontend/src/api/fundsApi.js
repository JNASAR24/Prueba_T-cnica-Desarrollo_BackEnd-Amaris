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

export function createSubscription(token, fundId, notificationChannel) {
  return request("/api/v1/subscriptions", {
    method: "POST",
    token,
    body: { fundId, notificationChannel }
  });
}

export function cancelSubscription(token, subscriptionId, notificationChannel) {
  return request(`/api/v1/subscriptions/${subscriptionId}`, {
    method: "DELETE",
    token,
    body: { notificationChannel }
  });
}

export function getTransactions(token) {
  return request("/api/v1/transactions", { token });
}
