// fetchEventos.js

// URL base do backend
import { apiUrl } from "../utils/config.js";
import { makeSortable } from "../utils/tableSorter.js";

// Função para obter os eventos
export async function fetchEventos() {
  if (document.getElementById("paginaIndex")) {
    const gridEventos = document.querySelector(".gridEventos");

    try {
      const response = await fetch(`${apiUrl}/evento/obtertodos`);
      if (!response.ok) throw new Error("Erro ao buscar eventos");
      const eventos = await response.json();
      renderEventosIndex(eventos);
    } catch (error) {
      gridEventos.innerHTML = `<p style="font-size: 2.5rem; color: red;">Erro ao tentar buscar os eventos!</p>`;
      console.error(error);
    }
  } else if (document.getElementById("paginaAdmin")) {
    try {
      const response = await fetch(`${apiUrl}/evento/obtertodos`);
      if (!response.ok) throw new Error("Erro ao buscar eventos");
      const eventos = await response.json();
      renderEventosAdmin(eventos);
    } catch (error) {
      alert(error.message);
    }
  }
}

// Função para renderizar os eventos na index.html
function renderEventosIndex(eventos) {
  const gridEventos = document.querySelector(".gridEventos");
  gridEventos.innerHTML = "";

  if (eventos.length === 0) {
    gridEventos.innerHTML = `<p style="font-size: 2.5rem; color: red;">Nenhum evento disponível!</p>`;
    return;
  } else {
    eventos.forEach((evento) => {
      gridEventos.innerHTML += `
        <div class="eventoCard">
          <div class="eventoInfo">
            <h2 class="eventoNome">${evento.nome}</h2>
            <p class="eventoDescricao">${evento.descricao}</p>
            <p class="eventoData">O evento inicia dia: ${formatarDataParaExibicao(
              evento.dataInicio
            )} às ${formatarHoraParaInputTime(evento.horaInicio)}</p>
            <p class="eventoData">O evento termina dia: ${formatarDataParaExibicao(
              evento.dataFim
            )} às ${formatarHoraParaInputTime(evento.horaFim)}</p>
            <p class="eventoLocal">Local: ${evento.localizacao}</p>
            <a href="pages/lotesEvento.html?id=${encodeURIComponent(evento.id)}&nome=${encodeURIComponent(
        evento.nome
      )}&descricao=${encodeURIComponent(evento.descricao)}&dataInicio=${encodeURIComponent(
        evento.dataInicio
      )}&horaInicio=${encodeURIComponent(evento.horaInicio)}&dataFim=${encodeURIComponent(
        evento.dataFim
      )}&horaFim=${encodeURIComponent(evento.horaFim)}&local=${encodeURIComponent(
        evento.localizacao
      )}" class="comprarBtn">Comprar Ingresso</a>
          </div>
        </div>
  `;
    });
  }
}

// Função para renderizar os eventos na admin.html
function renderEventosAdmin(eventos) {
  const telaInicial = document.querySelector(".telaAdmInicial");
  const form = document.getElementById("formAdm");
  const tableAdm = document.getElementById("tableAdm");

  telaInicial.innerHTML = "";

  // Limpa o conteúdo existente
  form.innerHTML = "";
  tableAdm.innerHTML = "";

  // Renderiza o formulário
  form.innerHTML = `
    <h1 style="display: flex; justify-content: center; font-size: 2.5rem;"> Gerenciamento dos Eventos </h1>
    <input type="hidden" id="id">
    <div class="input-group">
        <label for="nome">Nome:</label>
        <input type="text" id="nome" required>
    </div>
    <div class="input-group">
        <label for="descricao">Descrição:</label>
        <input type="text" id="descricao">
    </div>
    <div class="input-group">
        <label for="dataInicio">Data de Início:</label>
        <input type="date" id="dataInicio" required  max="2028-12-31">
    </div>
    <div class="input-group">
        <label for="horaInicio">Hora de Início:</label>
        <input type="time" id="horaInicio" required>
    </div>
    <div class="input-group">
        <label for="dataFim">Data de Término:</label>
        <input type="date" id="dataFim" required max="2028-12-31">
    </div>
    <div class="input-group">
        <label for="horaFim">Hora de Término:</label>
        <input type="time" id="horaFim" required>
    </div>
    <div class="input-group">
        <label for="localizacao">Localização:</label>
        <input type="text" id="localizacao" required>
    </div>
    <div class="button-group">
        <button type="submit" id="btnSalvarEvento" class="form-button save-button">Salvar</button>
        <button type="button" class="form-button clear-button" onclick="clearFormEvento()">Limpar</button>
    </div>
  `;

  // Renderiza a tabela
  tableAdm.innerHTML = `
    <thead>
      <tr>
        <th>Nome</th>
        <th>Descrição</th>
        <th>Data de Início</th>
        <th>Hora de Início</th>
        <th>Data de Término</th>
        <th>Hora de Término</th>
        <th>Localização</th>
        <th>Ações</th>
      </tr>
    </thead>
    <tbody>
      ${eventos
        .map(
          (evento) => `
        <tr>
          <td>${evento.nome}</td>
          <td>${evento.descricao}</td>
          <td>${formatarDataParaExibicao(evento.dataInicio)}</td>
          <td>${formatarHoraParaInputTime(evento.horaInicio)}</td>
          <td>${formatarDataParaExibicao(evento.dataFim)}</td>
          <td>${formatarHoraParaInputTime(evento.horaFim)}</td>
          <td>${evento.localizacao}</td>
          <td>
            <button class="btn-editar" onclick="editarEvento(${evento.id})">Editar</button>
            <button class="btn-deletar" onclick="deletarEvento(${evento.id}, '${evento.nome}')">Deletar</button>
          </td>
        </tr>
      `
        )
        .join("")}
    </tbody>
  `;

  // Aplica a função de ordenação na tabela
  makeSortable("tableAdm");
}

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formAdm");
  if (form) {
    form.addEventListener("submit", (event) => {
      if (event.submitter && event.submitter.id === "btnSalvarEvento") {
        event.preventDefault();
        salvarEvento(event);
      }
    });
  }
});

// Função para salvar ou atualizar um evento
async function salvarEvento(event) {
  event.preventDefault(); // Evita o envio padrão do formulário

  const id = document.getElementById("id").value; // ID (para edição)
  const nome = document.getElementById("nome").value;
  const descricao = document.getElementById("descricao").value;
  const dataInicio = document.getElementById("dataInicio").value;
  const horaInicio = document.getElementById("horaInicio").value;
  const dataFim = document.getElementById("dataFim").value;
  const horaFim = document.getElementById("horaFim").value;
  const localizacao = document.getElementById("localizacao").value;

  console.log(dataInicio);

  // Validação dos campos
  if (!nome || !descricao || !dataInicio || !horaInicio || !dataFim || !horaFim || !localizacao) {
    alert("Preencha todos os campos!");
    return;
  }

  if (dataInicio > dataFim) {
    alert("A data de início não pode ser maior que a data de término!");
    return;
  }

  if (dataInicio.toString() === dataFim.toString() && horaInicio >= horaFim) {
    alert("A hora de início não pode ser maior ou igual a hora de término no mesmo dia!");
    return;
  }

  const evento = {
    id,
    nome,
    descricao,
    dataInicio,
    horaInicio,
    dataFim,
    horaFim,
    localizacao,
  };

  console.log(evento);

  try {
    let response;
    if (id) {
      // Se houver um ID, é uma edição (PUT)
      response = await fetch(`${apiUrl}/evento/atualizar/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(evento),
      });
      alert("Evento editado com sucesso!");
      console.log(evento);
    } else {
      // Caso contrário, é um novo registro (POST)
      response = await fetch(`${apiUrl}/evento/cadastrar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(evento),
      });
      alert("Evento salvo com sucesso!");
    }

    if (!response.ok) throw new Error("Erro ao salvar o evento.");

    clearFormEvento(); // Limpa o formulário
    fetchEventos(); // Recarrega a lista
  } catch (error) {
    alert(error.message);
  }
}

// Função para preencher o formulário na edição
async function editarEvento(id) {
  try {
    const response = await fetch(`${apiUrl}/evento/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o evento.");
    const evento = await response.json();

    // Preenche os campos do formulário
    document.getElementById("id").value = evento.id || ""; // Verifique se está preenchendo corretamente
    document.getElementById("nome").value = evento.nome;
    document.getElementById("descricao").value = evento.descricao;
    document.getElementById("dataInicio").value = formatarDataParaInputDate(evento.dataInicio);
    document.getElementById("horaInicio").value = formatarHoraParaInputTime(evento.horaInicio);
    document.getElementById("dataFim").value = formatarDataParaInputDate(evento.dataFim);
    document.getElementById("horaFim").value = formatarHoraParaInputTime(evento.horaFim);
    document.getElementById("localizacao").value = evento.localizacao;
  } catch (error) {
    alert(error.message);
  }
}

// Função para limpar o formulário
function clearFormEvento() {
  document.getElementById("id").value = ""; // Limpa o ID
  document.getElementById("nome").value = "";
  document.getElementById("descricao").value = "";
  document.getElementById("dataInicio").value = "";
  document.getElementById("horaInicio").value = "";
  document.getElementById("dataFim").value = "";
  document.getElementById("horaFim").value = "";
  document.getElementById("localizacao").value = "";
}

// Função para deletar um evento
async function deletarEvento(id, nome) {
  const confirmacao = confirm(`Tem certeza que deseja deletar o evento ${nome}?`);
  if (!confirmacao) return;

  try {
    const response = await fetch(`${apiUrl}/evento/deletar/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) throw new Error("Erro ao deletar o evento.");

    alert("Evento deletado com sucesso!");
    fetchEventos(); // Recarrega a lista após a exclusão
  } catch (error) {
    alert(error.message);
  }
}

// Função para formatar a data para exibição (dd/mm/yyyy)
function formatarDataParaExibicao(dataArray) {
  if (!Array.isArray(dataArray) || dataArray.length !== 3) return "";
  const [ano, mes, dia] = dataArray;
  return `${String(dia).padStart(2, "0")}/${String(mes).padStart(2, "0")}/${ano}`;
}

// Função para formatar a data para input date (yyyy-MM-dd)
function formatarDataParaInputDate(dataArray) {
  if (!Array.isArray(dataArray) || dataArray.length !== 3) return "";
  const [ano, mes, dia] = dataArray;
  return `${ano}-${String(mes).padStart(2, "0")}-${String(dia).padStart(2, "0")}`;
}

// Função para formatar a hora para input time (HH:mm)
function formatarHoraParaInputTime(horaArray) {
  if (!Array.isArray(horaArray) || horaArray.length !== 2) return "";
  const [horas, minutos] = horaArray;
  return `${String(horas).padStart(2, "0")}:${String(minutos).padStart(2, "0")}`;
}

// Adiciona as funções ao escopo global
window.deletarEvento = deletarEvento;
window.editarEvento = editarEvento;
window.clearFormEvento = clearFormEvento;

if (document.getElementById("paginaIndex")) {
  fetchEventos();

  // Exemplo de atualizalção automática
  // setInterval(() => {
  //   fetchEventos();
  // }, 10000);
}
