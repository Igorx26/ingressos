// URL base do backend
import { apiUrl } from "../utils/config.js";
import { makeSortable } from "../utils/tableSorter.js";

// Função para obter os tipos de ingressos
export async function fetchTiposIngressos() {
  try {
    const response = await fetch(`${apiUrl}/tipoIngresso/obtertodos`);
    if (!response.ok) throw new Error("Erro ao buscar os tipos de ingressos");
    const tiposIngressos = await response.json();
    renderTiposIngressos(tiposIngressos);
  } catch (error) {
    alert(error.message);
  }
}

// Função para renderizar os tipos de ingressos
function renderTiposIngressos(tiposIngressos) {
  const telaInicial = document.querySelector(".telaAdmInicial");
  const form = document.getElementById("formAdm");
  const tableAdm = document.getElementById("tableAdm");

  telaInicial.innerHTML = "";

  // Limpa o conteúdo existente
  form.innerHTML = "";
  tableAdm.innerHTML = "";

  // Renderiza o formulário usando template strings
  form.innerHTML = `
    <h1 style="display: flex; justify-content: center; font-size: 2.5rem;"> Gerenciamento dos Tipos de Ingressos </h1>
    <input type="hidden" id="id">
    <div class="input-group">
        <label for="nome">Nome:</label>
        <input type="text" id="nome" required>
    </div>
    <div class="input-group">
        <label for="descricao">Descrição:</label>
        <input type="text" id="descricao" required>
    </div>
    <div class="button-group">
        <button type="submit" id="btnSalvarTipoIngresso" class="form-button save-button">Salvar</button>
        <button type="button" class="form-button clear-button" onclick="clearFormTipoIngresso()">Limpar</button>
    </div>
  `;

  // Renderiza a tabela usando template strings
  tableAdm.innerHTML = `
    <thead>
      <tr>
        <th>Nome</th>
        <th>Descrição</th>
        <th>Ações</th>
      </tr>
    </thead>
    <tbody>
      ${tiposIngressos
        .map(
          (tipoIngresso) => `
        <tr>
          <td>${tipoIngresso.nome}</td>
          <td>${tipoIngresso.descricao}</td>
          <td>
            <button class="btn-editar" onclick="editarTipoIngresso(${tipoIngresso.id})">Editar</button>
            <button class="btn-deletar" onclick="deletarTipoIngresso(${tipoIngresso.id}, '${tipoIngresso.nome}')">Deletar</button>
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
      if (event.submitter && event.submitter.id === "btnSalvarTipoIngresso") {
        event.preventDefault();
        salvarTipoIngresso(event);
      }
    });
  }
});

// Função para salvar ou atualizar um tipo de ingresso
async function salvarTipoIngresso(event) {
  event.preventDefault(); // Evita o envio padrão do formulário

  const id = document.getElementById("id").value; // ID (para edição)
  const nome = document.getElementById("nome").value;
  const descricao = document.getElementById("descricao").value;

  const tipoIngresso = { id, nome, descricao };

  try {
    let response;
    if (id) {
      // Se houver um ID, é uma edição (PUT)
      response = await fetch(`${apiUrl}/tipoIngresso/atualizar/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(tipoIngresso),
      });
      alert("Tipo de ingresso editado com sucesso!");
    } else {
      // Caso contrário, é um novo registro (POST)
      response = await fetch(`${apiUrl}/tipoIngresso/cadastrar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(tipoIngresso),
      });
      alert("Tipo de ingresso salvo com sucesso!");
    }

    if (!response.ok) throw new Error("Erro ao salvar o tipo de ingresso.");

    clearFormTipoIngresso(); // Limpa o formulário
    fetchTiposIngressos(); // Recarrega a lista
  } catch (error) {
    alert(error.message);
  }
}

// Função para preencher o formulário na edição
async function editarTipoIngresso(id) {
  try {
    const response = await fetch(`${apiUrl}/tipoIngresso/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o tipo de ingresso.");
    const tipoIngresso = await response.json();

    // Preenche o formulário com os dados do tipo de ingresso
    document.getElementById("id").value = tipoIngresso.id;
    document.getElementById("nome").value = tipoIngresso.nome;
    document.getElementById("descricao").value = tipoIngresso.descricao;
  } catch (error) {
    alert(error.message);
  }
}

// Função para limpar o formulário
function clearFormTipoIngresso() {
  document.getElementById("id").value = ""; // Limpa o ID
  document.getElementById("nome").value = "";
  document.getElementById("descricao").value = "";
}

// Função para deletar um tipo de ingresso
async function deletarTipoIngresso(id, nome) {
  const confirmacao = confirm(`Tem certeza que deseja deletar o tipo de ingresso ${nome}?`);
  if (!confirmacao) return;

  try {
    const response = await fetch(`${apiUrl}/tipoIngresso/deletar/${id}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) throw new Error("Erro ao deletar o tipo de ingresso.");

    alert("Tipo de ingresso deletado com sucesso!");
    fetchTiposIngressos(); // Recarrega a lista após a exclusão
  } catch (error) {
    alert(error.message);
  }
}

// Adiciona as funções ao escopo global
window.deletarTipoIngresso = deletarTipoIngresso;
window.editarTipoIngresso = editarTipoIngresso;
window.clearFormTipoIngresso = clearFormTipoIngresso;
