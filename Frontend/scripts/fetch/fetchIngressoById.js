// URL base do backend
import { apiUrl } from "../utils/config.js";

export async function fetchIngressoById(id) {
  try {
    const response = await fetch(`${apiUrl}/ingresso/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o ingresso.");
    const ingresso = await response.json();
    return ingresso;
  } catch (error) {
    alert(error.message);
    return null;
  }
}
