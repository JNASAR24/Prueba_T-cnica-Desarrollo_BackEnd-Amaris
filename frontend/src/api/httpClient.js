function resolveApiBaseUrl() {
  const configuredBaseUrl = import.meta.env.VITE_API_BASE_URL?.trim();
  if (configuredBaseUrl) {
    return configuredBaseUrl;
  }

  // En desarrollo local mantenemos el backend por defecto en 8081.
  if (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1") {
    return "http://localhost:8081";
  }

  // En despliegues https (CloudFront), consumir mismo origen evita mixed-content.
  return window.location.origin;
}

const API_BASE_URL = resolveApiBaseUrl();

/**
 * Cliente HTTP base.
 * Mantiene la lógica de fetch en un único lugar para evitar duplicación.
 */
export async function request(path, { method = "GET", token, body } = {}) {
  const headers = { "Content-Type": "application/json" };
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined
  });

  const contentType = response.headers.get("content-type") ?? "";
  const payload = contentType.includes("application/json")
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    const message =
      typeof payload === "object" && payload?.message
        ? payload.message
        : "No fue posible procesar la solicitud.";
    throw new Error(message);
  }

  return payload;
}
