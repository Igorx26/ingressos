package br.com.balada.ingressos.model;

import br.com.balada.ingressos.rest.dto.TipoIngressoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TipoIngresso")
public class TipoIngressoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O valor não pode ser vazio.")
    @NotNull(message = "O valor não pode ser nulo.")
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "descricao", length = 255,nullable = true)
    private String descricao;

    public TipoIngressoDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, TipoIngressoDTO.class);
    }
}
