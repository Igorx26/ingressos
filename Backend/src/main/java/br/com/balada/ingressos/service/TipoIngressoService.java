package br.com.balada.ingressos.service;

import br.com.balada.ingressos.exception.*;
import br.com.balada.ingressos.model.TipoIngressoModel;
import br.com.balada.ingressos.repository.TipoIngressoRepository;
import br.com.balada.ingressos.rest.dto.TipoIngressoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TipoIngressoService {

    @Autowired
    private TipoIngressoRepository tipoIngressoRepository;

    @Transactional(readOnly = true)
    public List<TipoIngressoDTO> obterTodos() {
        List<TipoIngressoModel> tipoIngressos = tipoIngressoRepository.findAll();
        return tipoIngressos.stream()
                .map(tipoIngresso -> tipoIngresso.toDTO())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoIngressoDTO obterPorId(Long id) {

        if (!tipoIngressoRepository.existsById(id)) {
            throw new RuntimeException("Evento não encontrado!");
        }
        Optional<TipoIngressoModel> evento = tipoIngressoRepository.findById(id);

        return evento.get().toDTO();
    }

    @Transactional
    public TipoIngressoDTO salvar(TipoIngressoModel novoTipoIngresso) {
        try {
            if (tipoIngressoRepository.existsByNome(novoTipoIngresso.getNome())){
                throw new RuntimeException("Tipo ingresso já cadastrado!");
            }
            return tipoIngressoRepository.save(novoTipoIngresso).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Criar o tipo ingresso.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Criar o tipo ingresso  violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Criar o tipo ingresso  violou uma regra de negócio! Verifique e tente novamente!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Criar o tipo ingresso gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public TipoIngressoDTO atualizar(TipoIngressoModel tipoIngressoExistente){
        try {
            if (!tipoIngressoRepository.existsById(tipoIngressoExistente.getId())){
                throw new RuntimeException("Tipo ingresso não encontrado!");
            }
            return tipoIngressoRepository.save(tipoIngressoExistente).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Atualizar o tipo ingresso.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Atualizar o tipo ingresso violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Atualizar o tipo ingresso violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Atualizar o tipo ingresso não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Atualizar o tipo ingresso gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public void deletar(Long id){

        Optional<TipoIngressoModel> tipoIngresso = Optional.of(new TipoIngressoModel());
        tipoIngresso = tipoIngressoRepository.findById(id);

        try {
            if (!tipoIngressoRepository.existsById(id)){
                throw new RuntimeException("Tipo ingresso não encontrado!");
            }
            tipoIngressoRepository.deleteById(id);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Deletar o tipo ingresso.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Deletar o tipo ingresso violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Deletar o tipo ingresso violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Deletar o tipo ingresso não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Deletar o tipo ingresso gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }
}
