package br.com.balada.ingressos.rest.dto;

import br.com.balada.ingressos.model.TipoIngressoModel;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class TipoIngressoDTO {

    private Long id;
    private String nome;
    private String descricao;

    public TipoIngressoModel toModel(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, TipoIngressoModel.class);
    }
}
