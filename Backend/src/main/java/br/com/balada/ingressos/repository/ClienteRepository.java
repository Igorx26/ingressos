package br.com.balada.ingressos.repository;

import br.com.balada.ingressos.model.ClienteModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

    boolean existsByCpf(String cpf);

    Optional<ClienteModel> findByCpf(String cpf);

    /*
    List<ClienteModel> findByNomeContainingIgnoreCase(String nome);
    List<ClienteModel> findByNomeContainingIgnoreCaseOrEmail(String nome, String email);
    List<ClienteModel> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);
    Page<ClienteModel> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    List<ClienteModel> findByDataNascimento(LocalDate dataNascimento);
    List<ClienteModel> findByDataNascimentoBetween(LocalDate dataInicio, LocalDate dataFim);
    long countByDataNascimentoBetween(LocalDate dataInicio, LocalDate dataFim);
    List<ClienteModel> findByDataNascimentoAfter(LocalDate data);
    List<ClienteModel> findByDataNascimentoBefore(LocalDate data);

    List<ClienteModel> findTop5ByOrderByDataNascimentoDesc(); //Retorna os 5 registros mais recentes
    List<ClienteModel> findFirst10ByNomeContaining(String nome); //Primeiros 10 registros que contêm o nome

    List<ClienteModel> findBySexo(String sexo);
    long countBySexo(String sexo);
     */
    void deleteById(Long id);
    void deleteByCpf(String cpf);
}
