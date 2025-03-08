package br.com.balada.ingressos.repository;

import br.com.balada.ingressos.model.TipoIngressoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoIngressoRepository extends JpaRepository<TipoIngressoModel, Long> {

    boolean existsByNome(String nome);
    List<TipoIngressoModel> findByNome(String nome);

}
