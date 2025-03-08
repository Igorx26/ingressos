package br.com.balada.ingressos.rest.controller;

import br.com.balada.ingressos.model.ClienteModel;
import br.com.balada.ingressos.rest.dto.ClienteDTO;
import br.com.balada.ingressos.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/obtertodos")
    public ResponseEntity<List<ClienteDTO>> obterTodos(){
        //List<ClienteDTO> clienteDTOList = clienteService.obterTodos();
        //return ResponseEntity.ok(clienteDTOList);
        return ResponseEntity.ok(clienteService.obterTodos());
    }

    @GetMapping("/obterporid/{id}")
    public ResponseEntity<ClienteDTO> obetPorId(@PathVariable Long id){
        return ResponseEntity.ok(clienteService.obterPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ClienteDTO> salvar(@RequestBody @Valid ClienteModel novoCliente){
        //ClienteDTO clienteCadastradoDTO = clienteService.salvar(novoCliente);
        //return ResponseEntity.ok(clienteCadastradoDTO);
        return ResponseEntity.ok(clienteService.salvar(novoCliente));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ClienteDTO> atualizar(@RequestBody @Valid ClienteModel clienteExistente){
        //ClienteDTO clienteExistenteDTO = clienteService.atualizar(clienteExistente);
        //return ResponseEntity.ok(clienteExistenteDTO);
        return ResponseEntity.ok(clienteService.atualizar(clienteExistente));
    }

    @DeleteMapping("/deletar/{cpf}")
    public void deletar(@PathVariable String cpf){
        clienteService.deletar(cpf);
    }

}
