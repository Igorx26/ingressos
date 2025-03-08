// fetchLotes.js

// URL base do backend
import { apiUrl } from "../utils/config.js";
import { makeSortable } from "../utils/tableSorter.js";

// Função para obter os lotes
export async function fetchLotes() {
  if (document.getElementById("paginaLotesEvento")) {
    // Está na index.html (ou outra página que não seja admin)
    const gridLotes = document.querySelector(".gridLotes");

    try {
      const response = await fetch(`${apiUrl}/lote/obtertodos`);
      if (!response.ok) throw new Error("Erro ao buscar lotes");
      const lotes = await response.json();
      renderLotesIndex(lotes); // Renderiza para a index.html
    } catch (error) {
      gridLotes.innerHTML = `<p style="color: red;">${error.message}</p>`;
    }
  } else if (document.getElementById("paginaAdmin")) {
    // Está na admin.html
    try {
      const response = await fetch(`${apiUrl}/lote/obtertodos`);
      if (!response.ok) throw new Error("Erro ao buscar lotes");
      const lotes = await response.json();
      renderLotesAdmin(lotes); // Renderiza para a admin.html
    } catch (error) {
      alert(error.message);
    }
  }
}

// Função para renderizar os lotes na index.html
function renderLotesIndex(lotes) {
  const gridLotes = document.querySelector(".gridLotes");
  gridLotes.innerHTML = ""; // Limpa o conteúdo existente

  // Obter dados da URL
  const params = new URLSearchParams(window.location.search);
  const dadosUrl = {
    id: params.get("id"),
    nome: params.get("nome"),
    descricao: params.get("descricao"),
    dataInicio: params.get("dataInicio"),
    horaInicio: params.get("horaInicio"),
    dataFim: params.get("dataFim"),
    horaFim: params.get("horaFim"),
    local: params.get("local"),
  };

  // Atualiza o título do evento no <h1>
  const tituloEvento = document.querySelector("#lotes h1");
  tituloEvento.textContent = `Lotes do Evento ${dadosUrl.nome}`;

  lotes.forEach((lote) => {
    // Calcular a quantidade de ingressos disponíveis
    const quantidadeDisponivel = lote.quantidade - lote.quantidadeVendida;

    if (quantidadeDisponivel > 0) {
      if (dadosUrl.id == lote.idEvento) {
        gridLotes.innerHTML += `
          <div class="loteCard">
            <div class="loteInfo">
              <h2 class="loteNome">${lote.nome}</h2>
              <p class="loteDescricao">Quantidade disponível: ${quantidadeDisponivel}</p>
              <p class="loteData">Preço: ${Number(lote.precoBase).toLocaleString("pt-BR", {
                style: "currency",
                currency: "BRL",
              })}</p>
              <p class="loteLocal">Início da venda: ${formatarDataParaExibicao(lote.dataInicioVenda)}</p>
              <p class="loteLocal">Fim da venda: ${formatarDataParaExibicao(lote.dataFimVenda)}</p>
              <a href="checkout.html?idlote=${encodeURIComponent(lote.id)}&idevento=${encodeURIComponent(
          lote.idEvento
        )}" class="comprarBtn">Comprar Ingresso</a>
            </div>
          </div>
        `;
      }
    } else {
      gridLotes.innerHTML += `
        <div class="loteCard">
          <div class="loteInfo">
            <h2 class="loteNome">${lote.nome}</h2>
            <p style="margin-top: 50px; font-size:25px; color:red;"> Lote esgotado </p>
          </div>
        </div>
        `;
    }
  });
}

// Função para renderizar os lotes na admin.html
async function renderLotesAdmin(lotes) {
  const telaInicial = document.querySelector(".telaAdmInicial");
  const form = document.getElementById("formAdm");
  const tableAdm = document.getElementById("tableAdm");

  telaInicial.innerHTML = "";

  // Limpa o conteúdo existente
  form.innerHTML = "";
  tableAdm.innerHTML = "";

  // Renderiza o formulário
  form.innerHTML = `
    <h1 style="display: flex; justify-content: center; font-size: 2.5rem;"> Gerenciamento dos Lotes </h1>
    <input type="hidden" id="id">
    <div class="input-group">
        <label for="nome">Nome:</label>
        <input type="text" id="nome" required>
    </div>
    <div class="input-group">
        <label for="quantidade">Quantidade disponível para venda:</label>
        <input type="number" id="quantidade" required placeholder="0">
    </div>
    <div class="input-group">
        <label for="precoBase">Preço dos ingressos desse lote:</label>
        <input type="number" id="precoBase" step="0.01" required placeholder="R$0,00">
    </div>
    <div class="input-group">
        <label for="dataInicioVenda">Data de Início da Venda:</label>
        <input type="date" id="dataInicioVenda" required  max="2028-12-31">
    </div>
    <div class="input-group">
        <label for="dataFimVenda">Data de Término da Venda:</label>
        <input type="date" id="dataFimVenda" required  max="2028-12-31">
    </div>
    <div class="input-group">
        <label for="status">Status:</label>
        <select id="status" required>
            <option value="ativo">Ativo</option>
            <option value="inativo">Inativo</option>
        </select>
    </div>
    <div class="input-group">
        <label for="idEvento">Evento:</label>
        <select id="idEvento" required></select>
    </div>
    <div class="input-group">
        <label for="idTipoIngresso">Tipo de Ingresso:</label>
        <select id="idTipoIngresso" required></select>
    </div>
    <div class="button-group">
        <button type="submit" id="btnSalvarLote" class="form-button save-button">Salvar</button>
        <button type="button" class="form-button clear-button" onclick="clearFormLote()">Limpar</button>
    </div>
  `;

  let eventos = [];
  let tiposIngressos = [];

  try {
    // Busca os eventos da API
    const responseEventos = await fetch(`${apiUrl}/evento/obtertodos`);
    if (!responseEventos.ok) throw new Error("Erro ao buscar eventos");
    eventos = await responseEventos.json();

    // Busca os tipos de ingressos da API
    const responseTiposIngressos = await fetch(`${apiUrl}/tipoIngresso/obtertodos`);
    if (!responseTiposIngressos.ok) throw new Error("Erro ao buscar tipos de ingressos");
    tiposIngressos = await responseTiposIngressos.json();
  } catch (error) {
    alert(error.message);
  }

  // Função para encontrar o nome do evento com base no ID
  const encontrarNomeEvento = (idEvento) => {
    const evento = eventos.find((evento) => evento.id === idEvento);
    return evento ? evento.nome : "Evento não encontrado";
  };

  // Função para encontrar o nome do tipo de ingresso com base no ID
  const encontrarNomeTipoIngresso = (idTipoIngresso) => {
    const tipoIngresso = tiposIngressos.find((tipo) => tipo.id === idTipoIngresso);
    return tipoIngresso ? tipoIngresso.nome : "Tipo de ingresso não encontrado";
  };

  // Renderiza a tabela
  tableAdm.innerHTML = `
    <thead>
      <tr>
        <th>Nome</th>
        <th>Quantidade</th>
        <th>Quantidade Vendida</th>
        <th>Preço Base</th>
        <th>Data Início Venda</th>
        <th>Data Fim Venda</th>
        <th>Status</th>
        <th>Evento</th>
        <th>Tipo de Ingresso</th>
        <th>Ações</th>
      </tr>
    </thead>
    <tbody>
      ${lotes
        .map(
          (lote) => `
        <tr>
          <td>${lote.nome}</td>
          <td>${lote.quantidade}</td>
          <td>${lote.quantidadeVendida}</td>
          <td>${Number(lote.precoBase).toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL",
          })}</td>
          <td>${formatarDataParaExibicao(lote.dataInicioVenda)}</td>
          <td>${formatarDataParaExibicao(lote.dataFimVenda)}</td>
          <td>${lote.quantidadeVendida >= lote.quantidade ? "Inativo" : "Ativo"}</td>
          <td>${encontrarNomeEvento(lote.idEvento)}</td>
          <td>${encontrarNomeTipoIngresso(lote.idTipoIngresso)}</td>
          <td>
            <button class="btn-editar" onclick="editarLote(${lote.id})">Editar</button>
            <button class="btn-deletar" onclick="deletarLote(${lote.id}, '${lote.nome}')">Deletar</button>
          </td>
        </tr>
      `
        )
        .join("")}
    </tbody>
  `;

  // Carrega os eventos e tipos de ingressos após renderizar o formulário
  carregarEventos();
  carregarTiposIngressos();

  // Aplica a função de ordenação na tabela
  makeSortable("tableAdm");
}

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formAdm");
  if (form) {
    form.addEventListener("submit", (event) => {
      if (event.submitter && event.submitter.id === "btnSalvarLote") {
        event.preventDefault();
        salvarLote(event);
      }
    });
  }
});

// Função para carregar os eventos no combobox
async function carregarEventos() {
  try {
    // Verifica se o elemento existe no DOM
    const selectEventos = document.getElementById("idEvento");

    // Busca os eventos da API
    const response = await fetch(`${apiUrl}/evento/obtertodos`);
    if (!response.ok) throw new Error("Erro ao buscar eventos");
    const eventos = await response.json();

    // Limpa o combobox antes de adicionar novas opções
    selectEventos.innerHTML = "";

    // Criar as opções
    selectEventos.innerHTML = eventos
      .map(
        (evento) => `
        <option value="${evento.id}">${evento.nome}</option>
      `
      )
      .join(""); // Junta todas as opções em uma única string
  } catch (error) {
    alert(error.message);
  }
}

// Função para carregar os tipos de ingressos no combobox
async function carregarTiposIngressos() {
  try {
    // Verifica se o elemento existe no DOM
    const selectTiposIngressos = document.getElementById("idTipoIngresso");

    // Busca os tipos de ingressos da API
    const response = await fetch(`${apiUrl}/tipoIngresso/obtertodos`);
    if (!response.ok) throw new Error("Erro ao buscar tipos de ingressos");
    const tiposIngressos = await response.json();

    // Limpa o combobox antes de adicionar novas opções
    selectTiposIngressos.innerHTML = "";

    // Criar as opções
    selectTiposIngressos.innerHTML = tiposIngressos
      .map(
        (tipo) => `
        <option value="${tipo.id}">${tipo.nome}</option>
      `
      )
      .join(""); // Junta todas as opções em uma única string
  } catch (error) {
    alert(error.message);
  }
}

// Função para salvar ou atualizar um lote
async function salvarLote(event) {
  event.preventDefault(); // Evita o envio padrão do formulário

  const id = document.getElementById("id").value; // ID (para edição)
  const nome = document.getElementById("nome").value;
  const quantidade = document.getElementById("quantidade").value;
  const precoBase = document.getElementById("precoBase").value;
  const dataInicioVenda = document.getElementById("dataInicioVenda").value;
  const dataFimVenda = document.getElementById("dataFimVenda").value;
  const status = document.getElementById("status").value;
  const idEvento = document.getElementById("idEvento").value;
  const idTipoIngresso = document.getElementById("idTipoIngresso").value;

  // Validação dos campos
  if (
    !nome ||
    !quantidade ||
    !precoBase ||
    !dataInicioVenda ||
    !dataFimVenda ||
    !status ||
    !idEvento ||
    !idTipoIngresso
  ) {
    alert("Preencha todos os campos!");
    return;
  }

  if (dataInicioVenda > dataFimVenda) {
    alert("A data de início da venda não pode ser maior que a data de término.");
    return;
  }

  // Cria um objeto com os dados do formulário
  const lote = {
    id,
    nome,
    quantidade,
    quantidadeVendida: 0,
    precoBase,
    dataInicioVenda,
    dataFimVenda,
    status: "Ativo",
    idEvento,
    idTipoIngresso,
  };

  try {
    let response;
    if (id) {
      // Se houver um ID, é uma edição (PUT)
      response = await fetch(`${apiUrl}/lote/atualizar/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(lote),
      });
      alert("Lote editado com sucesso!");
    } else {
      // Caso contrário, é um novo registro (POST)
      response = await fetch(`${apiUrl}/lote/cadastrar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(lote),
      });
      alert("Lote salvo com sucesso!");
    }

    if (!response.ok) throw new Error("Erro ao salvar o lote.");

    clearFormLote(); // Limpa o formulário
    fetchLotes(); // Recarrega a lista
  } catch (error) {
    alert(error.message);
  }
}

// Função para preencher o formulário na edição
async function editarLote(id) {
  try {
    const response = await fetch(`${apiUrl}/lote/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o lote.");
    const lote = await response.json();

    // Preenche o formulário com os dados do lote
    document.getElementById("id").value = lote.id;
    document.getElementById("nome").value = lote.nome;
    document.getElementById("quantidade").value = lote.quantidade;
    document.getElementById("precoBase").value = lote.precoBase;
    document.getElementById("dataInicioVenda").value = formatarDataParaInputDate(lote.dataInicioVenda);
    document.getElementById("dataFimVenda").value = formatarDataParaInputDate(lote.dataFimVenda);
    document.getElementById("status").value = lote.status;
    document.getElementById("idEvento").value = lote.idEvento;
    document.getElementById("idTipoIngresso").value = lote.idTipoIngresso;
  } catch (error) {
    alert(error.message);
  }
}

// Função para limpar o formulário
function clearFormLote() {
  document.getElementById("id").value = ""; // Limpa o ID
  document.getElementById("nome").value = "";
  document.getElementById("quantidade").value = "";
  document.getElementById("precoBase").value = "";
  document.getElementById("dataInicioVenda").value = "";
  document.getElementById("dataFimVenda").value = "";
  document.getElementById("status").value = "ativo";
  document.getElementById("idEvento").value = "";
  document.getElementById("idTipoIngresso").value = "";
}

// Função para deletar um lote
async function deletarLote(id, nome) {
  const confirmacao = confirm(`Tem certeza que deseja deletar o lote ${nome}?`);
  if (!confirmacao) return;

  try {
    const response = await fetch(`${apiUrl}/lote/deletar/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) throw new Error("Erro ao deletar o lote.");

    alert("Lote deletado com sucesso!");
    fetchLotes(); // Recarrega a lista após a exclusão
  } catch (error) {
    alert(error.message);
  }
}

// Função para formatar a data para exibição (dd/mm/yyyy)
function formatarDataParaExibicao(dataArray) {
  if (!Array.isArray(dataArray)) return "";
  const [ano, mes, dia] = dataArray;
  return `${String(dia).padStart(2, "0")}/${String(mes).padStart(2, "0")}/${ano}`;
}

// Função para formatar a data para input date (yyyy-MM-dd)
function formatarDataParaInputDate(dataArray) {
  if (!Array.isArray(dataArray)) return "";
  const [ano, mes, dia] = dataArray;
  return `${ano}-${String(mes).padStart(2, "0")}-${String(dia).padStart(2, "0")}`;
}

// Adiciona as funções ao escopo global
window.deletarLote = deletarLote;
window.editarLote = editarLote;
window.clearFormLote = clearFormLote;

if (document.getElementById("paginaLotesEvento")) {
  fetchLotes();

  // setInterval(() => {
  //   fetchLotes();
  // }, 2000);
}
