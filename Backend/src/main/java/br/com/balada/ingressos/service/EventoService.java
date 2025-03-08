package br.com.balada.ingressos.service;

import br.com.balada.ingressos.exception.*;
import br.com.balada.ingressos.model.EventoModel;
import br.com.balada.ingressos.model.TipoIngressoModel;
import br.com.balada.ingressos.repository.EventoRepository;
import br.com.balada.ingressos.rest.dto.EventoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Transactional(readOnly = true)
    public List<EventoDTO> obterTodos(){
        List<EventoModel> eventos = eventoRepository.findAll();
        return eventos.stream()
                .map(evento -> evento.toDTO())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventoDTO obterPorId(Long id) {

        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento não encontrado!");
        }
        Optional<EventoModel> evento = eventoRepository.findById(id);

        return evento.get().toDTO();
    }

    private boolean isEventoValidado(EventoModel novoEvento) {
        LocalDate dataInicio = novoEvento.getDataInicio();
        LocalDate dataFim = novoEvento.getDataFim();
        LocalTime horaInicio = novoEvento.getHoraInicio();
        LocalTime horaFim = novoEvento.getHoraFim();
        boolean eventoValidado = true;

        // Verifica se a data de início é anterior à data atual
        if (dataInicio.isBefore(LocalDate.now())) {
            eventoValidado = false;
            throw new RuntimeException("Data do evento inválida! A data de início não pode ser anterior à data atual.");
        }

        // Verifica se a data de início é posterior à data de término
        if (dataInicio.isAfter(dataFim)) {
            eventoValidado = false;
            throw new RuntimeException("Data do evento inválida! A data de início não pode ser posterior à data de término.");
        }

        // Verifica se a data de início é igual à data de término
        if (dataInicio.isEqual(dataFim)) {
            // Se for o mesmo dia, a hora de início não pode ser posterior à hora de término
            if (horaInicio.isAfter(horaFim)) {
                eventoValidado = false;
                throw new RuntimeException("Horário do evento inválido! A hora de início não pode ser posterior à hora de término no mesmo dia.");
            }
        }

        return eventoValidado;
    }

    @Transactional
    public EventoDTO salvar(EventoModel novoEvento){
        try {
            if (isEventoValidado(novoEvento)){
                if (eventoRepository.existsByNomeAndLocalizacaoAndDataInicioAndHoraInicio(
                        novoEvento.getNome(),
                        novoEvento.getLocalizacao(),
                        novoEvento.getDataInicio(),
                        novoEvento.getHoraInicio())) {
                    throw new RuntimeException("Evento já cadastrado!");
                }
                return eventoRepository.save(novoEvento).toDTO();
            } else {
                throw new RuntimeException("Cadastro do evento não validado! Verifique as datas informadas.");
            }
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Criar o evento: " + novoEvento.getNome());
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Criar o evento " + novoEvento.getNome() + " violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Criar o evento " + novoEvento.getNome() + " violou uma regra de negócio! Verifique e tente novamente!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Criar o evento " + novoEvento.getNome() + " gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public EventoDTO atualizar(EventoModel eventoExistente){
        try {
            if (!eventoRepository.existsById(eventoExistente.getId())){
                throw new RuntimeException("Evento não encontrado!");
            }
            return eventoRepository.save(eventoExistente).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Atualizar o evento: " + eventoExistente.getNome());
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Atualizar o evento " + eventoExistente.getNome() + " violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Atualizar o evento " + eventoExistente.getNome() + " violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Atualizar o evento " + eventoExistente.getNome() + " não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Atualizar o evento " + eventoExistente.getNome() + " gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public void deletar(Long id){

        Optional<EventoModel> evento = Optional.of(new EventoModel());
        evento = eventoRepository.findById(id);

        try {
            if (!eventoRepository.existsById(id)){
                throw new RuntimeException("Evento não encontrado!");
            }
            eventoRepository.deleteById(id);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Deletar o evento");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Deletar o evento violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Deletar o evento violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Deletar o evento não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Deletar o evento gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

}
