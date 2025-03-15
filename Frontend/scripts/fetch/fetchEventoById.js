// URL base do backend
import { apiUrl } from "../utils/config.js";

// Função para buscar o evento pelo ID
export async function fetchEventoById(id) {
  try {
    const response = await fetch(`${apiUrl}/evento/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o evento.");
    const evento = await response.json();
    return evento;
  } catch (error) {
    alert(error.message);
  }
}
