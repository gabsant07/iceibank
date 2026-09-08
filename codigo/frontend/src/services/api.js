export class ApiError extends Error {
  constructor(message, status, data) {
    super(message);
    this.status = status;
    this.data = data;
  }
}

export async function apiRequest(session, path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...options.headers
  };

  if (session?.token) {
    headers.Authorization = `Bearer ${session.token}`;
  }

  let response;
  try {
    response = await fetch(`${session.baseUrl}${path}`, { ...options, headers });
  } catch {
    throw new ApiError("Não foi possível conectar à agência. Verifique se o backend está rodando.", 0);
  }

  const data = await response.json().catch(() => null);
  if (!response.ok) {
    throw new ApiError(data?.message || `Erro HTTP ${response.status}`, response.status, data);
  }
  return data;
}

export function createBankApi(session) {
  const request = (path, options) => apiRequest(session, path, options);
  const post = (path, body) => request(path, { method: "POST", body: JSON.stringify(body) });

  return {
    currentAgency: () => request("/api/agencies/current"),
    listAccounts: () => request("/api/accounts"),
    findAccount: number => request(`/api/accounts/${number}`),
    createAccount: data => post("/api/accounts", data),
    deposit: (number, amount) => post(`/api/accounts/${number}/deposits`, { amount }),
    withdraw: (number, amount) => post(`/api/accounts/${number}/withdrawals`, { amount }),
    transfer: data => post("/api/transfers", data),
    history: number => request(`/api/accounts/${number}/history`),
    timeline: () => request("/api/timeline")
  };
}
