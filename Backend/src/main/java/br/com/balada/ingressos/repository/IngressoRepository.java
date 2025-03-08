package br.com.balada.ingressos.repository;

import br.com.balada.ingressos.model.EventoModel;
import br.com.balada.ingressos.model.IngressoModel;
import br.com.balada.ingressos.model.LoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IngressoRepository extends JpaRepository<IngressoModel, Long> {


    // boolean existsByIdClienteAndIdLote(Long idCliente, Long idLote);

    // Optional<IngressoModel> findByIdClienteAndIdLote(Long id, Long idLote);

    /*
    // Lista todos os ingressos vendidos/não vendidos de um determinado lote

    List<IngressoModel> findByStatusAndIdLote(String status, LoteModel idLote);

    // Conta quantos ingressos foram vendido em um determinado dia
    long countByDataCompra(LocalDateTime dataCompra); // Se eu quiser só a data?

    // Conta quantos ingressos foram usados/não usados de um determinado evento
    // long countByUsadoAndIdLoteAndIdEvento(Boolean usado, LoteModel idLote, LoteModel idEvento);
    */
}
