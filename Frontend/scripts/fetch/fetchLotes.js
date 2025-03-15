// URL base do backend
import { apiUrl } from "../utils/config.js";

// Função para obter os lotes
export async function fetchLotes() {
  try {
    const response = await fetch(`${apiUrl}/lote/obtertodos`);
    if (!response.ok) throw new Error("Erro ao buscar lotes");
    const lotes = await response.json();
    return lotes;
  } catch (error) {
    alert(error.message);
  }
}
