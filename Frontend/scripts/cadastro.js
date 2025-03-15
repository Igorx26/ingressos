// cadastro.js

// URL base do backend
import { apiUrl } from "../scripts/utils/config.js";
import { mascaraCelular } from "./utils/mascaraCelular.js";
import { mascaraCPF } from "./utils/mascaraCPF.js";

document.getElementById("cadastroForm").addEventListener("submit", async function (event) {
  event.preventDefault(); // Evita o envio do formulário

  // Dados do Cliente
  const nome = document.getElementById("nome").value;
  const cpf = document.getElementById("cpf").value;
  const celular = document.getElementById("celular").value;
  const email = document.getElementById("email").value;
  const sexo = document.getElementById("sexo").value;
  const dataNascimento = document.getElementById("dataNascimento").value;

  // Dados do Usuário
  const userName = document.getElementById("userName").value;
  const password = document.getElementById("password").value;

  // Validação dos campos
  if (!nome || !cpf || !celular || !email || !sexo || !dataNascimento || !userName || !password) {
    alert("Por favor, preencha todos os campos.");
    return;
  }

  const cliente = {
    nome: nome,
    cpf: cpf,
    celular: celular,
    email: email,
    sexo: sexo,
    dataNascimento: dataNascimento,
  };

  const usuario = {
    userName: userName,
    password: password,
    dataCriacao: new Date().toISOString().split("T")[0], // Data atual
    idCliente: null,
  };

  try {
    // Envia os dados do cliente para o backend
    const responseCliente = await fetch(`${apiUrl}/cliente/cadastrar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(cliente),
    });

    console.log("responseCliente", responseCliente);

    if (!responseCliente.ok) {
      throw new Error("Erro ao cadastrar cliente.");
    }

    const clienteSalvo = await responseCliente.json();
    console.log("Cliente cadastrado:", clienteSalvo);

    // Associa o ID do cliente ao usuário
    usuario.idCliente = clienteSalvo.id;

    // Envia os dados do usuário para o backend
    const responseUsuario = await fetch(`${apiUrl}/usuario/cadastrar`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usuario),
    });

    if (!responseUsuario.ok) {
      throw new Error("Erro ao cadastrar usuário.");
    }

    const usuarioSalvo = await responseUsuario.json();
    console.log("Usuário cadastrado:", usuarioSalvo);

    alert("Cadastro realizado com sucesso!");
    window.location.href = "login.html"; // Redireciona para a página de login
  } catch (error) {
    console.error("Erro:", error);
    alert("Erro ao cadastrar. Tente novamente.");
  }
});

mascaraCelular();
mascaraCPF();
