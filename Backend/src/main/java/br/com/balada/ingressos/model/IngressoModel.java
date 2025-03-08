package br.com.balada.ingressos.model;

import br.com.balada.ingressos.rest.dto.IngressoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Ingresso")
public class IngressoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O valor não pode ser nulo.")
    @Column(name ="precoFinal", nullable = false)
    @Min(value = 0, message = "O preço do ingresso deve ser maior que 0.")
    private Float precoFinal;

    @Column(name = "dataCompra", nullable = false)
    private LocalDate dataCompra;

    @Column(name= "horaCompra", nullable = false)
    private LocalTime horaCompra;

    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "status", length = 255, nullable = false)
    private String status;

    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "qrCode", length = 1500, nullable = true)
    private String qrCode;

    @NotNull(message = "O valor não pode ser nulo.")
    @Column(name = "usado", nullable = false)
    private Boolean usado;

    /*
    @ManyToOne
    @JoinColumn(name = "idCliente")
    */
    @NotNull(message = "O id de Cliente não pode ser nulo.")
    @Column(name = "idCliente", nullable = false)
    private Long idCliente;

    /*
    @ManyToOne(
    @JoinColumn(name = "idLote", nullable = false)
    */
    @NotNull(message = "O id de Lote não pode ser nulo.")
    @Column(name = "idLote", nullable = false)
    private Long idLote;

    public IngressoDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, IngressoDTO.class);
    }
}
