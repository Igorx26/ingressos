package br.com.balada.ingressos.rest.dto;

import br.com.balada.ingressos.model.EventoModel;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class EventoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalTime horaInicio;
    private LocalDate dataFim;
    private LocalTime horaFim;
    private String localizacao;

    public EventoModel toModel(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, EventoModel.class);
    }
}
