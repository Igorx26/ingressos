// URL base do backend
import { apiUrl } from "../utils/config.js";

// Função para obter os eventos
export async function fetchEventos() {
  try {
    const response = await fetch(`${apiUrl}/evento/obtertodos`);
    if (!response.ok) throw new Error("Erro ao buscar eventos");
    const eventos = await response.json();
    return eventos;
  } catch (error) {
    alert(error.message);
  }
}
