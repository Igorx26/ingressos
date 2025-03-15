// URL base do backend
import { apiUrl } from "../utils/config.js";
import { fetchClientes } from "../fetch/fetchClientes.js";
import { fetchClienteById } from "../fetch/fetchClienteById.js";
import { fetchEventoById } from "../fetch/fetchEventoById.js";
import { fetchLoteById } from "../fetch/fetchLoteById.js";

// Função para salvar o ingresso
async function salvarIngresso(idLote, idCliente) {
  try {
    const lote = await fetchLoteById(idLote);
    const cliente = await fetchClienteById(idCliente);

    const ingresso = {
      precoFinal: lote.precoBase,
      dataCompra: new Date().toISOString().split("T")[0], // Formato "YYYY-MM-DD"
      horaCompra: new Date().toLocaleTimeString("pt-BR", { hour12: false }), // Formato "HH:mm:ss"
      status: "Disponível",
      qrCode: "string",
      usado: false,
      idCliente: cliente.id,
      idLote: lote.id,
    };

    const response = await fetch(`${apiUrl}/ingresso/cadastrar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(ingresso),
    });

    if (!response.ok) throw new Error("Erro ao salvar o ingresso.");

    const novoIngresso = await response.json();
    return novoIngresso;
  } catch (error) {
    alert(error.message);
    return null;
  }
}

// Função para renderizar a página de checkout
async function renderCheckout() {
  const eventoInfo = document.querySelector(".eventoInfo");
  const listaClientes = document.querySelector(".listaClientes");

  // Obtém os parâmetros da URL
  const params = new URLSearchParams(window.location.search);
  const idLote = params.get("idlote");
  const idEvento = params.get("idevento");

  // Busca os dados do evento e do lote
  const lote = await fetchLoteById(idLote);
  const evento = await fetchEventoById(idEvento);

  // Renderiza as informações do evento e do lote
  eventoInfo.innerHTML = `
    <h3 class="eventoNome">${evento.nome}</h3>
    <p class="eventoData">Lote: ${lote.nome}</p>
    <p class="eventoData">Data do evento: ${new Date(
      evento.dataInicio
    ).toLocaleDateString()}</p>
    <p class="eventoLocal">Local: ${evento.localizacao}</p>
    <p class="eventoPreco">Preço: R$ ${Number(lote.precoBase).toLocaleString(
      "pt-BR",
      {
        style: "currency",
        currency: "BRL",
      }
    )}</p>
  `;

  // Busca e renderiza a lista de clientes
  const clientes = await fetchClientes();
  listaClientes.innerHTML = clientes
    .map(
      (cliente) => `
      <div class="clienteItem">
        <input type="radio" name="cliente" value="${cliente.id}" id="cliente-${
        cliente.id
      }">
        <label for="cliente-${cliente.id}">
          ${cliente.nome} - ${new Date(
        cliente.dataNascimento
      ).toLocaleDateString()}
        </label>
      </div>
    `
    )
    .join("");

  // Adiciona o evento de finalizar compra
  document
    .getElementById("finalizarCompraBtn")
    .addEventListener("click", async () => {
      const clienteSelecionado = document.querySelector(
        'input[name="cliente"]:checked'
      );
      if (!clienteSelecionado) {
        alert("Selecione um cliente para finalizar a compra.");
        return;
      }

      // Salva o ingresso e espera a confirmação
      const idCliente = clienteSelecionado.value;
      const novoIngresso = await salvarIngresso(idLote, idCliente);

      if (novoIngresso) {
        alert("Ingresso salvo com sucesso!");
        // Redireciona para a página de ingresso com os parâmetros necessários
        window.location.href = `ingresso.html?idingresso=${novoIngresso.id}&idevento=${idEvento}&idlote=${idLote}&idcliente=${idCliente}`;
      }
    });
}

renderCheckout();
