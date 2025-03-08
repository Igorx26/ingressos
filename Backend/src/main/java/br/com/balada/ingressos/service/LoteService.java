package br.com.balada.ingressos.service;

import br.com.balada.ingressos.exception.*;
import br.com.balada.ingressos.model.EventoModel;
import br.com.balada.ingressos.model.LoteModel;
import br.com.balada.ingressos.model.TipoIngressoModel;
import br.com.balada.ingressos.repository.LoteRepository;
import br.com.balada.ingressos.rest.dto.EventoDTO;
import br.com.balada.ingressos.rest.dto.LoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoteService {

    @Autowired
    private LoteRepository loteRepository;

    @Transactional(readOnly = true)
    public List<LoteDTO> obterTodos() {
        List<LoteModel> lotes = loteRepository.findAll();
        return lotes.stream()
                .map(lote -> lote.toDTO())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LoteDTO obterPorId(Long id) {

        if (!loteRepository.existsById(id)) {
            throw new RuntimeException("Evento não encontrado!");
        }
        Optional<LoteModel> lote = loteRepository.findById(id);

        return lote.get().toDTO();
    }

    // Método público para recuperar o lote por ID
    @Transactional(readOnly = true)
    public List<LoteDTO> obterTodosPorEvento(Long idEvento) {
        // Busca os lotes associados ao evento com o ID fornecido
        List<LoteModel> lotes = loteRepository.findByIdEvento(idEvento);

        // Converte a lista de LoteModel para LoteDTO
        return lotes.stream()
                .map(lote -> lote.toDTO())
                .collect(Collectors.toList());
    }

    // Método público para recuperar o lote por ID
    public LoteModel obterLotePorId(Long idLote) {
        return loteRepository.findById(idLote)
                .orElseThrow(() -> new RuntimeException("Lote não encontrado!"));
    }

    private boolean isDadosValidados(LoteModel novoLote){
        boolean validada = true;

        // Verifica se existe lote com mesmo nome no mesmo evento
        if (loteRepository.existsByNomeAndIdEvento(novoLote.getNome(), novoLote.getIdEvento())) {
            validada = false;
            throw new RuntimeException("Lote já cadastrado!");
        }

        // Verifica se dataFimVenda está antes ou depois da dataInicioVenda
        if(novoLote.getDataFimVenda().isBefore(novoLote.getDataInicioVenda())){
            validada = false;
            throw new RuntimeException("Data final da venda do lote não pode ser menor que a data de início!");
        }

        return validada;
    }

    @Transactional
    public LoteDTO salvar(LoteModel novoLote) {
        try {
           if (isDadosValidados(novoLote)) {
               LoteModel lote = new LoteModel();

               lote.setNome(novoLote.getNome());
               lote.setQuantidade(novoLote.getQuantidade());
               lote.setQuantidadeVendida(0L);
               lote.setPrecoBase(novoLote.getPrecoBase());
               lote.setDataInicioVenda(novoLote.getDataInicioVenda());
               lote.setDataFimVenda(novoLote.getDataFimVenda());
               lote.setStatus("Disponível");
               lote.setIdEvento(novoLote.getIdEvento());
               lote.setIdTipoIngresso(novoLote.getIdTipoIngresso());

               return loteRepository.save(lote).toDTO();

           } else {
               throw new RuntimeException("Não foi possível fazer o cadastro do lote, verifique as informações passadas!");
           }
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Criar o lote.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Criar o lote violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Criar o lote violou uma regra de negócio! Verifique e tente novamente!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Criar o lote gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public LoteDTO atualizar(LoteModel loteExistente){
        try {
            if (!loteRepository.existsById(loteExistente.getId())){
                throw new RuntimeException("Lote não encontrado!");
            }
            return loteRepository.save(loteExistente).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Atualizar o lote.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Atualizar o lote violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Atualizar o lote violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Atualizar o lote não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Atualizar o lote gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public void deletar(Long id){

        Optional<LoteModel> lote = Optional.of(new LoteModel());
        lote = loteRepository.findById(id);

        try {
            if (!loteRepository.existsById(id)){
                throw new RuntimeException("Lote não encontrado!");
            }
            loteRepository.deleteById(id);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Deletar o lote.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Deletar o lote violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Deletar o lote violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Deletar o lote não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Deletar o lote gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }
}
