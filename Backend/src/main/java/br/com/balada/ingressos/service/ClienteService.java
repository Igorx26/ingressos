package br.com.balada.ingressos.service;

import br.com.balada.ingressos.exception.*;
import br.com.balada.ingressos.model.ClienteModel;
import br.com.balada.ingressos.model.TipoIngressoModel;
import br.com.balada.ingressos.repository.ClienteRepository;
import br.com.balada.ingressos.rest.dto.ClienteDTO;
import br.com.balada.ingressos.rest.dto.TipoIngressoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteDTO> obterTodos(){
        List<ClienteModel> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> cliente.toDTO())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO obterPorId(Long id) {

        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado!");
        }
        Optional<ClienteModel> cliente = clienteRepository.findById(id);

        return cliente.get().toDTO();
    }

    @Transactional
    public ClienteDTO salvar(ClienteModel novoCliente){
        try {
            if (clienteRepository.existsByCpf(novoCliente.getCpf())){
                throw new RuntimeException("CPF já cadastrado!");
            }
            return clienteRepository.save(novoCliente).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Criar o cliente: " + novoCliente.getNome());
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Criar o cliente " + novoCliente.getNome() + " violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Criar o cliente " + novoCliente.getNome() + " violou uma regra de negócio! Verifique e tente novamente!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Criar o cliente " + novoCliente.getNome() + " gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public ClienteDTO atualizar(ClienteModel clienteExistente){
        try {
            if (!clienteRepository.existsById(clienteExistente.getId())){
                throw new RuntimeException("Cliente não encontrado!");
            }
            return clienteRepository.save(clienteExistente).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Atualizar o cliente: " + clienteExistente.getNome());
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Atualizar o cliente " + clienteExistente.getNome() + " violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Atualizar o cliente " + clienteExistente.getNome() + " violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Atualizar o cliente " + clienteExistente.getNome() + " não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Atualizar o cliente " + clienteExistente.getNome() + " gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public void deletar(String cpf){
        Optional<ClienteModel> cliente = Optional.of(new ClienteModel());
        cliente = clienteRepository.findByCpf(cpf);

        try {
            if (!clienteRepository.existsByCpf(cpf)){
                throw new RuntimeException("Tipo ingresso não encontrado!");
            }
            clienteRepository.deleteByCpf(cpf);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Deletar o cliente: ");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Deletar o cliente violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Deletar o cliente violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Deletar o cliente não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Deletar o cliente gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

}
