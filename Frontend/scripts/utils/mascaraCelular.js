// Função para máscara de Celular
export function mascaraCelular() {
  const campoCelular = document.getElementById("celular");

  campoCelular.addEventListener("keypress", (event) => {
    const tecla = event.key;
    if (!/\d/.test(tecla)) {
      event.preventDefault(); // Bloqueia a tecla se não for um número
    }

    let tamanhoCampoCelular = campoCelular.value.length;

    if (tamanhoCampoCelular === 0) {
      campoCelular.value += "(";
    } else if (tamanhoCampoCelular === 3) {
      campoCelular.value += ")";
    } else if (tamanhoCampoCelular === 9) {
      campoCelular.value += "-";
    }
  });
}
