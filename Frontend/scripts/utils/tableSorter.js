// tablerSorter.js

export function makeSortable(tableId) {
  const table = document.getElementById(tableId);
  if (!table) return;

  const headers = table.querySelectorAll("th");

  // Estado de ordenação da tabela
  const state = {
    currentColumn: null,
    ascending: true,
  };

  headers.forEach((header, index) => {
    // Verifica se a coluna é ordenável
    if (header.textContent !== "Ações") {
      // Adiciona classe e estilos para indicar que é ordenável
      header.classList.add("sortable");
      header.style.cursor = "pointer";
      header.style.position = "relative";

      // Cria indicador de ordenação
      const indicator = document.createElement("span");
      indicator.style.marginLeft = "5px";
      indicator.style.opacity = "0";
      indicator.textContent = "▲";
      header.appendChild(indicator);

      // Adiciona evento de clique
      header.addEventListener("click", () => {
        // Remove indicadores de outras colunas
        headers.forEach((h) => {
          if (h !== header) {
            const ind = h.querySelector("span");
            if (ind) ind.style.opacity = "0";
          }
        });

        const rows = Array.from(table.querySelectorAll("tbody tr"));

        // Verifica se estamos ordenando a mesma coluna
        if (state.currentColumn === index) {
          // Inverte a direção
          state.ascending = !state.ascending;
          rows.reverse();

          // Atualiza o indicador
          indicator.textContent = state.ascending ? "▲" : "▼";
        } else {
          // Nova coluna, ordena ascendente
          state.currentColumn = index;
          state.ascending = true;

          // Ordena as linhas
          rows.sort((rowA, rowB) => {
            const cellA = rowA.querySelectorAll("td")[index].textContent.trim().toLowerCase();
            const cellB = rowB.querySelectorAll("td")[index].textContent.trim().toLowerCase();

            // Tenta converter para número se ambos forem numéricos
            if (!isNaN(cellA) && !isNaN(cellB)) {
              return Number(cellA) - Number(cellB);
            }

            // Ordenação padrão como texto
            return cellA.localeCompare(cellB);
          });

          // Atualiza o indicador
          indicator.textContent = "▲";
        }

        // Mostra o indicador
        indicator.style.opacity = "1";

        // Reconstrói a tabela com as linhas ordenadas
        const tbody = table.querySelector("tbody");
        rows.forEach((row) => tbody.appendChild(row));
      });
    }
  });
}
