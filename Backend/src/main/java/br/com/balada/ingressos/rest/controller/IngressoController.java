package br.com.balada.ingressos.rest.controller;

import br.com.balada.ingressos.model.IngressoModel;
import br.com.balada.ingressos.rest.dto.IngressoDTO;
import br.com.balada.ingressos.service.IngressoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ingresso")
public class IngressoController {

    @Autowired
    private IngressoService ingressoService;

    @GetMapping("/obtertodos")
    public ResponseEntity<List<IngressoDTO>> obterTodos(){
        return ResponseEntity.ok(ingressoService.obterTodos());
    }

    @GetMapping("/obterporid/{id}")
    public ResponseEntity<IngressoDTO> obterPorClienteELote(@PathVariable Long id) {
        return ResponseEntity.ok(ingressoService.obterPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<IngressoDTO> salvar(@RequestBody @Valid IngressoModel novoIngresso){
        return ResponseEntity.ok(ingressoService.salvar(novoIngresso));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<IngressoDTO> atualizar(@RequestBody @Valid IngressoModel ingressoExistente){
        return ResponseEntity.ok(ingressoService.atualizar(ingressoExistente));
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id){
        ingressoService.deletar(id);
    }
}
