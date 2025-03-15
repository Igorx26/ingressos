// URL base do backend
import { apiUrl } from "../utils/config.js";

// Função para obter todos os usuarios
export async function fetchUsuarios() {
  try {
    const response = await fetch(`${apiUrl}/usuario/obtertodos`);
    if (!response.ok) throw new Error("Erro ao buscar usuarios");
    const usuarios = await response.json();
    return usuarios;
  } catch (error) {
    alert(error.message);
  }
}
