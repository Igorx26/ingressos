package br.com.balada.ingressos.repository;

import br.com.balada.ingressos.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository <UsuarioModel, Long> {

    boolean existsByUserName(String userName);

    // Lista os usuarios por data de criação
    List<UsuarioModel> findByDataCriacao(LocalDateTime dataCriacao);

    void deleteByPassword(String password);
}
