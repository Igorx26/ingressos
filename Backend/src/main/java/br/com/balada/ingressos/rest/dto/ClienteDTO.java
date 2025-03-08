package br.com.balada.ingressos.rest.dto;

import br.com.balada.ingressos.model.ClienteModel;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) para transferir informações sobre um cliente.
 *
 * Esta classe é utilizada para representar dados de um cliente e converter entre o DTO e o modelo de entidade.
 */
@Data
public class ClienteDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String celular;
    private String email;
    private String sexo;
    private LocalDate dataNascimento;

    /**
     * Converte o objeto de transferência de dados {@link ClienteDTO} em uma entidade {@link ClienteModel}.
     *
     * @return {@link ClienteModel} representando a entidade.
     */
    public ClienteModel toModel(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ClienteModel.class);
    }

}