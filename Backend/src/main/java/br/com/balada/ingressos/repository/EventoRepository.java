package br.com.balada.ingressos.repository;

import br.com.balada.ingressos.model.EventoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<EventoModel, Long> {

    boolean existsByNomeAndLocalizacaoAndDataInicioAndHoraInicio(String nome, String localizacao, LocalDate dataInicio, LocalTime horaInicio);

    // Seleciona todos os eventos pelo nome
    /*
    List<EventoModel> findByNome(String nome);
    List<EventoModel> findByNomeContainingIgnoreCase(String nome);

    List<EventoModel> findByLocalizacao(String localizacao);
    List<EventoModel> findByLocalizacaoContainingIgnoreCase(String localizacao);

    List<EventoModel> findByDataHoraInicio(LocalDateTime dataHoraInicio);
    List<EventoModel> findByDataHoraInicioBetween(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim);
    List<EventoModel> findByDataHoraInicioAfter(LocalDateTime dataHoraInicio);

    long countByDataHoraInicioBetween(LocalDateTime inicio, LocalDateTime fim); // Contagem de eventos por período
    */

    void deleteById(Long id);
    void deleteByNome(String cpf);

}
