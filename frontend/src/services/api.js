import { apiRequest } from "./apiClient";

function crud(base) {
  return {
    list: () => apiRequest(base),
    get: (id) => apiRequest(`${base}/${id}`),
    create: (body) => apiRequest(base, { method: "POST", body }),
    update: (id, body) => apiRequest(`${base}/${id}`, { method: "PUT", body }),
    remove: (id) => apiRequest(`${base}/${id}`, { method: "DELETE" }),
  };
}

export const authApi = {
  login: (email, password) =>
    apiRequest("/api/auth/login", { method: "POST", body: { email, password } }),
};

export const rolesApi = crud("/api/roles");
export const usuariosApi = crud("/api/usuarios");
export const familiasApi = crud("/api/familias-producto");
export const productosApi = crud("/api/productos");
export const stocksApi = {
  ...crud("/api/stocks"),
  incrementar: (id, cantidad) =>
    apiRequest(`/api/stocks/${id}/incrementar?cantidad=${cantidad}`, { method: "PUT" }),
  reducir: (id, cantidad) =>
    apiRequest(`/api/stocks/${id}/reducir?cantidad=${cantidad}`, { method: "PUT" }),
};
export const clientesApi = crud("/api/clientes");
export const pedidosApi = {
  ...crud("/api/pedidos-online"),
  registrar: (body) => apiRequest("/api/pedidos-online/registrar", { method: "POST", body }),
};
export const pagosApi = {
  ...crud("/api/pagos-online"),
  registrar: (body) => apiRequest("/api/pagos-online/registrar", { method: "POST", body }),
};
export const ventasApi = {
  ...crud("/api/ventas"),
  registrar: (body) => apiRequest("/api/ventas/registrar", { method: "POST", body }),
};
export const movimientosApi = crud("/api/movimientos-stock");
export const informesApi = {
  ventas: () => apiRequest("/api/informes/ventas"),
};
