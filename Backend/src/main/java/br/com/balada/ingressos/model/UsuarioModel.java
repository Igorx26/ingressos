package br.com.balada.ingressos.model;

import br.com.balada.ingressos.rest.dto.UsuarioDTO;
import jakarta.persistence.*;
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
@Table(name = "Usuario")
public class UsuarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "userName", length = 255, nullable = false, unique = true)
    private String userName;

    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "dataCriacao", nullable = false)
    private LocalDate dataCriacao;

    /*
    @OneToOne
    @JoinColumn(name = "idCliente", nullable = false)
    */
    @NotNull(message = "O id de Cliente não pode ser nulo.")
    @Column(name = "idCliente", nullable = false, unique = true)
    private Long idCliente;

    public UsuarioDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, UsuarioDTO.class);
    }
}
