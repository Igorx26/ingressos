// URL base do backend
import { apiUrl } from "../utils/config.js";

// Função para buscar todos os clientes
export async function fetchClientes() {
  try {
    const response = await fetch(`${apiUrl}/cliente/obtertodos`);
    if (!response.ok) throw new Error("Erro ao buscar os clientes.");
    const clientes = await response.json();
    return clientes;
  } catch (error) {
    alert(error.message);
  }
}
