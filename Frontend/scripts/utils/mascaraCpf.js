// Função para máscara de CPF
export function mascaraCPF() {
  let campoCPF = document.getElementById("cpf");

  campoCPF.addEventListener("keypress", (event) => {
    const tecla = event.key;
    if (!/\d/.test(tecla)) {
      event.preventDefault(); // Bloqueia a tecla se não for um número
    }

    let tamanhoCampoCpf = campoCPF.value.length;

    if (tamanhoCampoCpf === 3 || tamanhoCampoCpf === 7) {
      campoCPF.value += ".";
    } else if (tamanhoCampoCpf === 11) {
      campoCPF.value += "-";
    }
  });
}
