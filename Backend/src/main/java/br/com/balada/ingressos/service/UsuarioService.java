package br.com.balada.ingressos.service;

import br.com.balada.ingressos.exception.*;
import br.com.balada.ingressos.model.UsuarioModel;
import br.com.balada.ingressos.repository.UsuarioRepository;
import br.com.balada.ingressos.rest.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> obterTodos() {
        List<UsuarioModel> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuario -> usuario.toDTO())
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDTO salvar(UsuarioModel novoUsuario) {
        try {
            if (usuarioRepository.existsByUserName(novoUsuario.getUserName())){
                throw new RuntimeException("Nome de Usuário indisponível!");
            } else {
                UsuarioModel usuario = new UsuarioModel();

                usuario.setUserName(novoUsuario.getUserName());
                usuario.setPassword(novoUsuario.getPassword());
                usuario.setDataCriacao(LocalDate.now());
                usuario.setIdCliente(novoUsuario.getIdCliente());

                return usuarioRepository.save(usuario).toDTO();
            }

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Criar o usuario.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Criar o usuario  violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Criar o usuario  violou uma regra de negócio! Verifique e tente novamente!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Criar o usuario gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public UsuarioDTO atualizar(UsuarioModel usuarioExistente){
        try {
            if (!usuarioRepository.existsById(usuarioExistente.getId())){
                throw new RuntimeException("Usuario não encontrado!");
            }
            return usuarioRepository.save(usuarioExistente).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Atualizar o usuario.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Atualizar o usuario violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Atualizar o usuario violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Atualizar o usuario não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Atualizar o usuario gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public void deletar(UsuarioModel usuarioExistente){
        try {
            if (!usuarioRepository.existsById(usuarioExistente.getId())){
                throw new RuntimeException("Usuario não encontrado!");
            }
            usuarioRepository.delete(usuarioExistente);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Deletar o usuario.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Deletar o usuario violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Deletar o usuario violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Deletar o usuario não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Deletar o usuario gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }
}
