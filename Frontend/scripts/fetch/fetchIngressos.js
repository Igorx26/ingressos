// URL base do backend
import { apiUrl } from "../utils/config.js";

export async function fetchIngressos() {
  try {
    const response = await fetch(`${apiUrl}/ingresso/obtertodos`);
    if (!response.ok) throw new Error("Erro ao buscar ingressos");
    const ingressos = await response.json();
    return ingressos;
  } catch (error) {
    alert(error.message);
  }
}
