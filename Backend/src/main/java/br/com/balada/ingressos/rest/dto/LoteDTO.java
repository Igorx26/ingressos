package br.com.balada.ingressos.rest.dto;

import br.com.balada.ingressos.model.LoteModel;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
public class LoteDTO {

    private Long id;
    private String nome;
    private Long quantidade;
    private Long quantidadeVendida;
    private Float precoBase;
    private LocalDate dataInicioVenda;
    private LocalDate dataFimVenda;
    private String status;
    private Long idEvento;
    private Long idTipoIngresso;

    public LoteModel toModel(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, LoteModel.class);
    }
}
