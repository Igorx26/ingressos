package br.com.balada.ingressos.model;

import br.com.balada.ingressos.rest.dto.LoteDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Lote")
public class LoteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O valor não pode ser vazio.")
    @NotNull(message = "O valor não pode ser nulo.")
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @NotNull(message = "O valor não pode ser nulo.")
    @Min(value = 1, message = "O evento deve ter pelo menos 1 lote cadastrado.")
    @Column(name = "quantidade", length = 255, nullable = false)
    private Long quantidade;

    @NotNull(message = "O valor não pode ser nulo.")
    @Column(name = "quantidadeVendida", length = 255, nullable = false)
    private Long quantidadeVendida;

    @NotNull(message = "O valor não pode ser nulo.")
    @Min(value = 0, message = "O preço base não deve ser negativo.")
    @Column(name = "precoBase", length = 255, nullable = false)
    private Float precoBase;

    @NotNull(message = "O valor não pode ser nulo.")
    @FutureOrPresent(message = "O Lote não pode ser cadastrado em uma data que já passou.")
    @Column(name = "dataInicioVenda", length = 255, nullable = false)
    private LocalDate dataInicioVenda;

    @NotNull(message = "O valor não pode ser nulo.")
    @FutureOrPresent(message = "O Lote não pode ser cadastrado em uma data que já passou.")
    @Column(name = "dataFimVenda", length = 255, nullable = false)
    private LocalDate dataFimVenda;

    @NotBlank(message = "O valor não pode ser vazio.")
    @NotNull(message = "O valor não pode ser nulo.")
    @Column(name = "status", length = 255, nullable = false)
    private String status;

    /*
    @ManyToOne
    @JoinColumn(name = "idEvento", nullable = false)
    */
    @NotNull(message = "O id de Evento não pode ser nulo.")
    @Column(name = "idEvento", nullable = false)
    private Long idEvento;

    /*
    @ManyToOne
    @JoinColumn(name = "idTipoIngresso", nullable = false)
     */
    @NotNull(message = "O id de TipoIngresso não pode ser nulo.")
    @Column(name = "idTipoIngresso", nullable = false)
    private Long idTipoIngresso;

    public LoteDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, LoteDTO.class);
    }
}
