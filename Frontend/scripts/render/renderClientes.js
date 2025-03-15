// URL base do backend
import { apiUrl } from "../utils/config.js";
import { fetchClientes } from "../fetch/fetchClientes.js";
import { fetchUsuarios } from "../fetch/fetchUsuarios.js";
import { mascaraCelular } from "../utils/mascaraCelular.js";
import { mascaraCPF } from "../utils/mascaraCPF.js";
import { makeSortable } from "../utils/tableSorter.js";

// Função para obter todos os clientes
export async function renderClientes() {
  try {
    const clientes = await fetchClientes();
    const usuarios = await fetchUsuarios();
    renderClientesAdmin(clientes, usuarios);
  } catch (error) {
    alert(error.message);
  }
}

// Função para renderizar os clientes na admin.html
function renderClientesAdmin(clientes, usuarios) {
  const telaInicial = document.querySelector(".telaAdmInicial");
  const form = document.getElementById("formAdm");
  const tableAdm = document.getElementById("tableAdm");

  // Limpa o texto inicial
  telaInicial.innerHTML = "";

  // Limpa o conteúdo existente
  form.innerHTML = "";
  tableAdm.innerHTML = "";

  // Renderiza o formulário
  form.innerHTML = `
  <h1 style="display: flex; justify-content: center; font-size: 2.5rem;"> Gerenciamento de Clientes </h1>
  <input type="hidden" id="idCliente"> <!-- ID do cliente -->
  <input type="hidden" id="idUsuario"> <!-- ID do usuário associado ao cliente -->
  <div class="input-group">
      <label for="nome">Nome:</label>
      <input type="text" id="nome" required>
  </div>
  <div class="input-group">
      <label for="cpf">CPF:</label>
      <input type="text" id="cpf" minlength="14" maxlength="14" required>
  </div>
  <div class="input-group">
      <label for="celular">Celular:</label>
      <input type="text" id="celular" minlength="14" maxlength="14" required>
  </div>
  <div class="input-group">
      <label for="email">Email:</label>
      <input type="email" id="email" required>
  </div>
  <div class="input-group">
      <label for="sexo">Sexo:</label>
      <select id="sexo" required>
          <option value="M">Masculino</option>
          <option value="F">Feminino</option>
      </select>
  </div>
  <div class="input-group">
      <label for="dataNascimento">Data de Nascimento:</label>
      <input type="date" id="dataNascimento" required max="${new Date().toISOString().split("T")[0]}">
  </div>
  <div class="input-group">
      <label for="userName">Nome de Usuário:</label>
      <input type="text" id="userName" required>
  </div>
  <div class="input-group">
      <label for="password">Senha:</label>
      <input type="password" id="password" required>
  </div>
  <div class="button-group">
      <button type="submit" id="btnSalvarCliente" class="form-button save-button">Salvar</button>
      <button type="button" class="form-button clear-button" onclick="clearFormCliente()">Limpar</button>
  </div>
`;

  // Renderiza a tabela
  tableAdm.innerHTML = `
 <thead>
   <tr>
     <th>Nome</th>
     <th>CPF</th>
     <th>Celular</th>
     <th>Email</th>
     <th>Sexo</th>
     <th>Data de Nascimento</th>
     <th>Nome de Usuário</th>
     <th>Ações</th>
   </tr>
 </thead>
 <tbody>
   ${clientes
     .map((cliente) => {
       const usuario = usuarios.find((u) => u.idCliente === cliente.id);
       return `
           <tr>
             <td>${cliente.nome}</td>
             <td>${cliente.cpf}</td>
             <td>${cliente.celular}</td>
             <td>${cliente.email}</td>
             <td>${cliente.sexo === "M" ? "Masculino" : "Feminino"}</td>
             <td>${formatarDataParaExibicao(cliente.dataNascimento)}</td>
             <td>${usuario ? usuario.userName : "N/A"}</td>
             <td>
               <button class="btn-editar" onclick="editarCliente(${cliente.id})">Editar</button>
               <button class="btn-deletar" onclick="deletarCliente('${cliente.cpf}', '${
         cliente.nome
       }')">Deletar</button>
             </td>
           </tr>
         `;
     })
     .join("")}
 </tbody>
`;

  // Aplica as máscaras aos campos de CPF e Celular
  const campoCPF = document.getElementById("cpf");
  const campoCelular = document.getElementById("celular");

  if (campoCPF) mascaraCPF(campoCPF);
  if (campoCelular) mascaraCelular(campoCelular);

  // Aplica a função de ordenação na tabela
  makeSortable("tableAdm");
}

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formAdm");
  if (form) {
    form.addEventListener("submit", (event) => {
      if (event.submitter && event.submitter.id === "btnSalvarCliente") {
        event.preventDefault();
        salvarCliente(event);
      }
    });
  }
});

// Função para salvar ou atualizar um cliente
export async function salvarCliente(event) {
  event.preventDefault(); // Evita o envio padrão do formulário

  const idCliente = document.getElementById("idCliente").value; // ID do cliente (para edição)
  const idUsuario = document.getElementById("idUsuario").value; // ID do usuario (para edição)
  const nome = document.getElementById("nome").value;
  const cpf = document.getElementById("cpf").value;
  const celular = document.getElementById("celular").value;
  const email = document.getElementById("email").value;
  const sexo = document.getElementById("sexo").value;
  const dataNascimento = converterDataParaArray(document.getElementById("dataNascimento").value);
  const userName = document.getElementById("userName").value;
  const password = document.getElementById("password").value;

  const cliente = {
    id: idCliente,
    nome,
    cpf,
    celular,
    email,
    sexo,
    dataNascimento,
  };

  const usuario = {
    id: idUsuario,
    userName,
    password,
    dataCriacao: new Date().toISOString(),
    idCliente: idCliente, // Associa o usuário ao cliente
  };

  try {
    let response;
    if (cliente.id) {
      // Se houver um ID, é uma edição (PUT)
      response = await fetch(`${apiUrl}/cliente/atualizar/${idCliente}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(cliente),
      });

      // Busca o ID do usuário associado ao cliente
      const usuarios = await fetchUsuarios();
      const usuarioExistente = usuarios.find((u) => u.idCliente == cliente.id);
      if (usuarioExistente) {
        usuario.id = usuarioExistente.id; // Usa o ID do usuário existente
        await fetch(`${apiUrl}/usuario/atualizar/${usuarioExistente.id}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(usuario),
        });
      } else {
        throw new Error("Usuário associado ao cliente não encontrado.");
      }

      alert("Cliente e usuário editados com sucesso!");
    } else {
      // Caso contrário, é um novo registro (POST)
      response = await fetch(`${apiUrl}/cliente/cadastrar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(cliente),
      });
      const novoCliente = await response.json();
      usuario.idCliente = novoCliente.id; // Associa o usuário ao novo cliente
      await fetch(`${apiUrl}/usuario/cadastrar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(usuario),
      });
      alert("Cliente e usuário cadastrados com sucesso!");
    }

    clearFormCliente(); // Limpa o formulário
    renderClientes(); // Recarrega a lista
  } catch (error) {
    alert(error.message);
  }
}

// Função para limpar o formulário
function clearFormCliente() {
  document.getElementById("idCliente").value = ""; // Limpa o ID do cliente
  document.getElementById("idUsuario").value = ""; // Limpa o ID do usuário
  document.getElementById("nome").value = "";
  document.getElementById("cpf").value = "";
  document.getElementById("celular").value = "";
  document.getElementById("email").value = "";
  document.getElementById("sexo").value = "M";
  document.getElementById("dataNascimento").value = "";
  document.getElementById("userName").value = "";
  document.getElementById("password").value = "";
}

// Função para deletar um cliente
async function deletarCliente(cpf, nome) {
  const confirmacao = confirm(`Tem certeza que deseja deletar o cliente ${nome}?`);
  if (!confirmacao) return;

  try {
    const response = await fetch(`${apiUrl}/cliente/deletar/${cpf}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) throw new Error("Erro ao deletar o cliente.");

    alert("Cliente deletado com sucesso!");
    renderClientes(); // Recarrega a lista após a exclusão
  } catch (error) {
    alert(error.message);
  }
}

// Função para preencher o formulário na edição
async function editarCliente(id) {
  try {
    const response = await fetch(`${apiUrl}/cliente/obterporid/${id}`);
    if (!response.ok) throw new Error("Erro ao buscar o cliente.");
    const cliente = await response.json();

    // Preenche o formulário com os dados do cliente
    document.getElementById("idCliente").value = cliente.id;
    document.getElementById("nome").value = cliente.nome;
    document.getElementById("cpf").value = cliente.cpf;
    document.getElementById("celular").value = cliente.celular;
    document.getElementById("email").value = cliente.email;
    document.getElementById("sexo").value = cliente.sexo;
    document.getElementById("dataNascimento").value = formatarDataParaInputDate(cliente.dataNascimento);

    // Busca o usuário associado ao cliente
    const usuarios = await fetchUsuarios();
    const usuario = usuarios.find((u) => u.idCliente === cliente.id);
    if (usuario) {
      document.getElementById("idUsuario").value = usuario.id; // Preenche o ID do usuário
      document.getElementById("userName").value = usuario.userName;
      document.getElementById("password").value = usuario.password;
    }
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

// Função para converter data do formato "YYYY-MM-DD" para [YYYY, MM, DD]
function converterDataParaArray(dataString) {
  if (!dataString) return null;
  const [ano, mes, dia] = dataString.split("-").map(Number);
  return [ano, mes, dia];
}

// Adiciona as funções ao escopo global
window.deletarCliente = deletarCliente;
window.editarCliente = editarCliente;
window.clearFormCliente = clearFormCliente;
