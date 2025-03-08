package br.com.balada.ingressos.model;

import br.com.balada.ingressos.rest.dto.EventoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Evento")
public class EventoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "descricao", length = 255, nullable = true)
    private String descricao;

    @FutureOrPresent(message = "O evento não pode ser cadastrado em uma data que já passou.")
    @Column(name = "dataInicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "horaInicio", nullable = false)
    private LocalTime horaInicio;

    @FutureOrPresent(message = "O evento não pode ser cadastrado em uma data que já passou.")
    @Column(name = "dataFim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "horaFim", nullable = false)
    private LocalTime horaFim;

    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "localizacao", length = 255, nullable = false)
    private String localizacao;

    public EventoDTO toDTO() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, EventoDTO.class);
    }
}