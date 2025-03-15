// URL base do backend
import { apiUrl } from "../utils/config.js";
import { fetchIngressoById } from "../fetch/fetchIngressoById.js";
import { fetchEventoById } from "../fetch/fetchEventoById.js";
import { fetchLoteById } from "../fetch/fetchLoteById.js";
import { fetchClienteById } from "../fetch/fetchClienteById.js";

// Função para renderizar a página do ingresso
async function renderIngresso() {
  const params = new URLSearchParams(window.location.search);
  const idIngresso = params.get("idingresso");
  const idEvento = params.get("idevento");
  const idLote = params.get("idlote");
  const idCliente = params.get("idcliente");

  const evento = await fetchEventoById(idEvento);
  const lote = await fetchLoteById(idLote);
  const cliente = await fetchClienteById(idCliente);

  // Busca o ingresso recém-criado
  const ingresso = await fetchIngressoById(idIngresso);

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
