// URL base do backend
import { apiUrl } from "./utils/config.js";

// Função para buscar o evento pelo ID
async function getEventoById(id) {
  try {
    const response = await fetch(`${apiUrl}/evento/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o evento.");
    const evento = await response.json();
    return evento;
  } catch (error) {
    alert(error.message);
  }
}

// Função para buscar o lote pelo ID
async function getLoteById(id) {
  try {
    const response = await fetch(`${apiUrl}/lote/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o lote.");
    const lote = await response.json();
    return lote;
  } catch (error) {
    alert(error.message);
  }
}

// Função para buscar o cliente pelo ID
async function getClienteById(id) {
  try {
    const response = await fetch(`${apiUrl}/cliente/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o cliente.");
    const cliente = await response.json();
    return cliente;
  } catch (error) {
    alert(error.message);
  }
}

// Função para buscar o ingresso mais recente pelo ID do cliente e ID do lote
async function getIngressoById(id) {
  try {
    // Adapte esta URL de acordo com a sua API
    const response = await fetch(`${apiUrl}/ingresso/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o ingresso.");
    const ingresso = await response.json();
    return ingresso;
  } catch (error) {
    alert(error.message);
    return null;
  }
}

// Função para renderizar a página de ingresso
async function renderIngresso() {
  const params = new URLSearchParams(window.location.search);
  const idIngresso = params.get("idingresso");
  const idEvento = params.get("idevento");
  const idLote = params.get("idlote");
  const idCliente = params.get("idcliente");

  const evento = await getEventoById(idEvento);
  const lote = await getLoteById(idLote);
  const cliente = await getClienteById(idCliente);

  // Busca o ingresso recém-criado
  const ingresso = await getIngressoById(idIngresso);

  document.getElementById("eventoNome").textContent = evento.nome;
  document.getElementById("loteNome").textContent = lote.nome;
  document.getElementById("eventoData").textContent = new Date(evento.dataInicio).toLocaleDateString();
  document.getElementById("eventoLocal").textContent = evento.localizacao;
  document.getElementById("clienteNome").textContent = cliente.nome;

  // Exibe o QR Code como uma imagem (se disponível)
  if (ingresso && ingresso.qrCode) {
    const qrCodeImg = document.getElementById("qrCodeImg");
    qrCodeImg.src = `data:image/png;base64,${ingresso.qrCode}`;
    qrCodeImg.alt = "QR Code do Ingresso";
  }
}

renderIngresso();
