// URL base do backend
import { apiUrl } from "../utils/config.js";

// Função para buscar o cliente pelo ID
export async function fetchClienteById(id) {
  try {
    const response = await fetch(`${apiUrl}/cliente/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o cliente.");
    const cliente = await response.json();
    return cliente;
  } catch (error) {
    alert(error.message);
  }
}
