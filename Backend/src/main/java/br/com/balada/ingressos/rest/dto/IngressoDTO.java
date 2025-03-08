package br.com.balada.ingressos.rest.dto;

import br.com.balada.ingressos.model.IngressoModel;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class IngressoDTO {

    private Long id;
    private Float precoFinal;
    private LocalDate dataCompra;
    private LocalTime horaCompra;
    private String status;
    private String qrCode;
    private Boolean usado;
    private Long idCliente;
    private Long idLote;

    public IngressoModel toModel(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, IngressoModel.class);
    }
}
