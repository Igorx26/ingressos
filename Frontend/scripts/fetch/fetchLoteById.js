// URL base do backend
import { apiUrl } from "../utils/config.js";

// Função para buscar o lote pelo ID
export async function fetchLoteById(id) {
  try {
    const response = await fetch(`${apiUrl}/lote/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o lote.");
    const lote = await response.json();
    return lote;
  } catch (error) {
    alert(error.message);
  }
}
