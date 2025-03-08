// Validação do formulário de login
document
  .getElementById("loginForm")
  .addEventListener("submit", function (event) {
    event.preventDefault(); // Evita o envio do formulário

    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    // Validação simples
    if (!email || !senha) {
      alert("Por favor, preencha todos os campos.");
      return;
    }

    alert("Login realizado com sucesso!");
    window.location.href = "../index.html"; // Redireciona para a página inicial
  });
