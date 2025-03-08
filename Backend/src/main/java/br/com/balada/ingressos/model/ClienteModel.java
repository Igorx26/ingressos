package br.com.balada.ingressos.model;

import br.com.balada.ingressos.rest.dto.ClienteDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data // Gera todos os getters e setters
@AllArgsConstructor // Gera o construtor com sobrecarga
@NoArgsConstructor // Gera o construtor sem sobrecarga
@Entity // Define a classe como entidade/modelo
@Table(name = "Cliente")
public class ClienteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Size(min = 14, max = 14, message = "O CPF deve conter exatamente 14 caracteres - 000.000.000-00")
    @Column(name = "cpf", length = 14, nullable = false, unique = true)
    @CPF(message = "CPF inválido, verifique os números inseridos.")
    private String cpf;

    @NotBlank(message = "O valor não pode ser vazio.")
    @Size(min = 14, max = 14, message = "O celular deve conter exatamente 14 caracteres - (00)00000-0000")
    @Column(name = "celular", length = 14, nullable = true)
    private String celular;

    @Email
    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @NotNull(message = "O valor não pode ser nulo.")
    @NotBlank(message = "O valor não pode ser vazio.")
    @Size(min = 1, max = 1, message = "O sexo deve conter exatamente 1 caracter. Ex: M ou F")
    @Column(name = "sexo", length = 1, nullable = false)
    private String sexo;

    @Past(message = "A data de nascimento deve ser pelo menos um anterior ao dia atual.")
    @Column(name = "dataNascimento", nullable = false)
    private LocalDate dataNascimento;

    /**
     * Converte a entidade {@link ClienteModel} para um objeto de transferência de dados {@link ClienteDTO}.
     *
     * @return {@link ClienteDTO} representando os dados do Cliente.
     */
    public ClienteDTO toDTO(){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, ClienteDTO.class);
    }
}
