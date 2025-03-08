package br.com.balada.ingressos.service;

import br.com.balada.ingressos.exception.*;
import br.com.balada.ingressos.model.IngressoModel;
import br.com.balada.ingressos.model.LoteModel;
import br.com.balada.ingressos.repository.IngressoRepository;
import br.com.balada.ingressos.rest.dto.ClienteDTO;
import br.com.balada.ingressos.rest.dto.EventoDTO;
import br.com.balada.ingressos.rest.dto.IngressoDTO;
import br.com.balada.ingressos.rest.dto.LoteDTO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngressoService {

    @Autowired
    private IngressoRepository ingressoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private LoteService loteService;

    // Método para gerar código único baseado em LocalDateTime
    /*
    private String gerarCodigoUnico() {
        // Obtém a data e hora atual
        LocalDateTime dataAtual = LocalDateTime.now();

        // Formata a data no padrão desejado
        String dataFormatada = dataAtual.format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));

        // Gera um número aleatório de duas casas com Math.random()
        int numeroAleatorio = (int) (Math.random() * 100);

        // Formata o número para sempre ter duas casas
        String codigoAleatorio = String.format("%02d", numeroAleatorio);

        // Combina data, simbolo "+" e número aleatório
        return dataFormatada + "+" + codigoAleatorio;
    }
     */



    @Transactional(readOnly = true)
    public List<IngressoDTO> obterTodos() {
        List<IngressoModel> ingressos = ingressoRepository.findAll();
        return ingressos.stream()
                .map(ingresso -> ingresso.toDTO())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IngressoDTO obterPorId(Long id) {

        if (!ingressoRepository.existsById(id)) {
            throw new RuntimeException("Ingresso não encontrado!");
        }
        Optional<IngressoModel> ingresso = ingressoRepository.findById(id);

        return ingresso.get().toDTO();
    }

    private boolean isVendaValidada(IngressoModel novoIngresso) {

        LoteDTO lote = loteService.obterPorId(novoIngresso.getIdLote());

        boolean validada = true;
        Long quantidade = lote.getQuantidade();
        Long quantidadeVendida = lote.getQuantidadeVendida();

        // Validação de quantidade vendida
        if (quantidadeVendida >= quantidade) {
            validada = false;  // Venda não validada
            throw new RuntimeException("Quantidade máxima de ingressos do lote vendida!");
        }

        return validada;
    }

    @Transactional
    public IngressoDTO salvar(IngressoModel novoIngresso) {
        try {
            // Verifica se a venda está validada
            if (isVendaValidada(novoIngresso)) {
                IngressoModel ingresso = new IngressoModel();

                // Obter o lote
                LoteModel lote = loteService.obterLotePorId(novoIngresso.getIdLote());

                // Obter o cliente
                ClienteDTO cliente = clienteService.obterPorId(novoIngresso.getIdCliente());

                // Obter o evento
                EventoDTO evento = eventoService.obterPorId(lote.getIdEvento());

                ingresso.setPrecoFinal(lote.getPrecoBase());
                ingresso.setDataCompra(LocalDate.now());  // Definindo a data de compra
                ingresso.setHoraCompra(LocalTime.now());
                ingresso.setStatus("Disponível");
                ingresso.setIdCliente(novoIngresso.getIdCliente());
                ingresso.setIdLote(novoIngresso.getIdLote());
                ingresso.setUsado(false);

                // Incremeta a quantidade de ingressos vendida no lote
                lote.setQuantidadeVendida(lote.getQuantidadeVendida() + 1);

                // Formatar a data para exibição
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String dataFormatada = ingresso.getDataCompra().format(formatter);

                // Monta uma string personalizada com várias informações para o QR Code
                String qrCodeData = String.format(
                        "Evento: %s | Lote: %s | Cliente: %s | Data de Compra: %s",
                        evento.getNome(),
                        lote.getNome(),
                        cliente.getNome(),
                        dataFormatada
                );

                String qrCodeBase64 = gerarQrCode(qrCodeData);  // Chama o método para gerar o QR Code
                ingresso.setQrCode(qrCodeBase64);

                // Salva o ingresso no repositório e retorna o DTO correspondente
                return ingressoRepository.save(ingresso).toDTO();
            } else {
                throw new RuntimeException("Venda não validada! Verifique a quantidade disponível.");
            }

        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Criar o ingresso.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Criar o ingresso violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Criar o ingresso violou uma regra de negócio! Verifique e tente novamente!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Criar o ingresso gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public IngressoDTO atualizar(IngressoModel ingressoExistente){
        try {
            if (!ingressoRepository.existsById(ingressoExistente.getId())){
                throw new RuntimeException("Ingresso não encontrado!");
            }
            return ingressoRepository.save(ingressoExistente).toDTO();
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Atualizar o ingresso.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Atualizar o ingresso violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Atualizar o ingresso violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Atualizar o ingresso não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Atualizar o ingresso gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    @Transactional
    public void deletar(Long id){
        Optional<IngressoModel> ingresso = Optional.of(new IngressoModel());
        ingresso = ingressoRepository.findById(id);

        try {
            if (!ingressoRepository.existsById(id)){
                throw new RuntimeException("Evento não encontrado!");
            }
            ingressoRepository.deleteById(id);
        } catch (DataIntegrityException e) {
            throw new DataIntegrityException("Erro! Deletar o ingresso.");
        } catch (ConstraintException e) {
            throw new ConstraintException("Erro! Deletar o ingresso violou uma restrição de dados! Verifique e tente novamente!");
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Erro! Deletar o ingresso violou uma regra de negócio! Verifique e tente novamente!");
        } catch (ObjectNotFoundException e) {
            throw new ObjectNotFoundException("Erro! Deletar o ingresso não foi encontrado!");
        } catch (SQLException e) {
            throw new SQLException("Erro! Deletar o ingresso gerou um erro de conexão com o banco de dados! Verifique e tente novamente!");
        }
    }

    private String gerarQrCode(String data) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 250, 250); // Gera a matriz do QR Code
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream(); // Cria um stream de saída
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream); // Converte a matriz para PNG
            byte[] pngData = pngOutputStream.toByteArray(); // Obtém os bytes da imagem PNG
            return Base64.getEncoder().encodeToString(pngData); // Converte a imagem em uma string Base64
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Erro ao gerar QR Code!", e);
        }
    }


}
