// URL base do backend
import { apiUrl } from "../utils/config.js";

// Função para obter os tipos de ingressos
export async function fetchTiposIngressos() {
  try {
    const response = await fetch(`${apiUrl}/tipoIngresso/obtertodos`);
    if (!response.ok) throw new Error("Erro ao buscar os tipos de ingressos");
    const tiposIngressos = await response.json();
    return tiposIngressos;
  } catch (error) {
    alert(error.message);
  }
}
