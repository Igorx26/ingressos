// URL base do backend
import { apiUrl } from "../utils/config.js";
import { makeSortable } from "../utils/tableSorter.js";

// Função para obter todos os ingressos com dados relacionados
export async function fetchIngressos() {
  try {
    // Busca todos os ingressos
    const responseIngressos = await fetch(`${apiUrl}/ingresso/obtertodos`);
    if (!responseIngressos.ok) throw new Error("Erro ao buscar ingressos");
    const ingressos = await responseIngressos.json();

    // Busca todos os lotes
    const responseLotes = await fetch(`${apiUrl}/lote/obtertodos`);
    if (!responseLotes.ok) throw new Error("Erro ao buscar lotes");
    const lotes = await responseLotes.json();

    // Busca todos os eventos
    const responseEventos = await fetch(`${apiUrl}/evento/obtertodos`);
    if (!responseEventos.ok) throw new Error("Erro ao buscar eventos");
    const eventos = await responseEventos.json();

    // Busca todos os clientes
    const responseClientes = await fetch(`${apiUrl}/cliente/obtertodos`);
    if (!responseClientes.ok) throw new Error("Erro ao buscar clientes");
    const clientes = await responseClientes.json();

    // Junta todas as informações de cada ingresso em um único objeto
    const ingressosCompletos = ingressos.map((ingresso) => {
      const lote = lotes.find((l) => l.id === ingresso.idLote);
      const evento = eventos.find((e) => e.id === lote.idEvento);
      const cliente = clientes.find((c) => c.id === ingresso.idCliente);

      return {
        ...ingresso,
        lote,
        evento,
        cliente,
      };
    });

    console.log(ingressosCompletos);

    // Exemplo de retorno
    // [
    //   {
    //     id: 1,
    //     precoFinal,
    //     dataCompra: "2025-08-03",
    //     horaCompra,
    //     status,
    //     qrCode,
    //     usado,
    //     idLote: 1,
    //     idCliente: 1,
    //     lote: { id: 1, idEvento: 1, nome: "Lote 1" },
    //     evento: { id: 1, nome: "Evento A" },
    //     cliente: { id: 1, nome: "Cliente X" },
    //   },
    //   {
    //     id: 2,
    //     precoFinal,
    //     dataCompra: "2025-08-03",
    //     horaCompra,
    //     status,
    //     qrCode,
    //     usado,
    //     idLote: 2,
    //     idCliente: 2,
    //     lote: { id: 2, idEvento: 1, nome: "Lote 2" },
    //     evento: { id: 1, nome: "Evento A" },
    //     cliente: { id: 2, nome: "Cliente Y" },
    //   },
    // ];

    renderIngressosAdmin(ingressosCompletos, eventos);
  } catch (error) {
    alert(error.message);
  }
}

// Função para renderizar os ingressos na admin.html
async function renderIngressosAdmin(ingressos, eventos) {
  const telaInicial = document.querySelector(".telaAdmInicial");
  const form = document.getElementById("formAdm");
  const tableAdm = document.getElementById("tableAdm");

  // Limpa o texto inicial
  telaInicial.innerHTML = "";

  // Limpa o conteúdo existente
  form.innerHTML = "";
  tableAdm.innerHTML = "";

  // Renderiza o formulário de filtro
  form.innerHTML = `
    <h1 style="display: flex; justify-content: center; font-size: 2.5rem;"> Visualizar Ingressos Vendidos</h1>
    <div class="input-group">
        <label for="evento">Evento:</label>
        <select id="evento" required>
            <option value="">Selecione um evento</option>
            ${eventos.map((evento) => `<option value="${evento.id}">${evento.nome}</option>`).join("")}
        </select>
    </div>
    <div class="button-group">
        <button type="button" id="btnBuscarIngressos" class="form-button save-button">Buscar</button>
    </div>
  `;

  // Filtra os ingressos ao clicar no botão
  const btnBuscar = document.getElementById("btnBuscarIngressos");
  btnBuscar.addEventListener("click", () => {
    const eventoId = document.getElementById("evento").value;
    filtrarIngressos(ingressos, eventoId);
  });

  // Renderiza a tabela inicialmente vazia
  tableAdm.innerHTML = `
    <thead>
      <tr>
        <th>Lote</th>
        <th>Cliente</th>
        <th>Data da Compra</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
  `;

  // Aplica a função de ordenação na tabela
  makeSortable("tableAdm");
}

// Função para filtrar ingressos por evento
function filtrarIngressos(ingressos, eventoId) {
  const tbody = document.querySelector("#tableAdm tbody");
  tbody.innerHTML = "";

  const ingressosFiltrados = ingressos.filter((ingresso) => {
    return eventoId ? ingresso.evento.id == eventoId : false;
  });

  if (ingressosFiltrados.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="3">Nenhum ingresso encontrado para o evento selecionado.</td>
      </tr>
    `;
  } else {
    ingressosFiltrados.forEach((ingresso) => {
      tbody.innerHTML += `
        <tr>
          <td>${ingresso.lote.nome}</td>
          <td>${ingresso.cliente.nome}</td>
          <td>${new Date(ingresso.dataCompra).toLocaleDateString()}</td>
        </tr>
      `;
    });
  }
}
