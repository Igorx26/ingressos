package br.com.balada.ingressos.repository;

import br.com.balada.ingressos.model.EventoModel;
import br.com.balada.ingressos.model.LoteModel;
import br.com.balada.ingressos.model.TipoIngressoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<LoteModel, Long> {

    boolean existsByNomeAndIdEvento(String nome, Long idEvento);

    /*
    // Conta quantos lotes em um determinado evento
    long countByIdAndIdEvento(Long id, EventoModel idEvento);
    */

    // Seleciona todos os lotes de um determinado evento
    List<LoteModel> findByIdEvento(Long idEvento);

    /*
    // Selecionas quais lotes são de um determinado tipo
    List<LoteModel> findByIdTipoIngresso(TipoIngressoModel idTipoIngresso);

     */
}
