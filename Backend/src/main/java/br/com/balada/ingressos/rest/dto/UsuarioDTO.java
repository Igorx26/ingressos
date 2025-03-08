package br.com.balada.ingressos.rest.dto;

import br.com.balada.ingressos.model.UsuarioModel;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
public class UsuarioDTO {

    private Long id;
    private String userName;
    private String password;
    private LocalDate dataCriacao;
    private Long idCliente;

    public UsuarioModel toModel(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, UsuarioModel.class);
    }
}
