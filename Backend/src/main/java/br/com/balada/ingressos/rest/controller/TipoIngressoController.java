package br.com.balada.ingressos.rest.controller;

import br.com.balada.ingressos.model.TipoIngressoModel;
import br.com.balada.ingressos.rest.dto.TipoIngressoDTO;
import br.com.balada.ingressos.service.TipoIngressoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipoIngresso")
public class TipoIngressoController {

    @Autowired
    private TipoIngressoService tipoIngressoService;

    @GetMapping("/obtertodos")
    public ResponseEntity<List<TipoIngressoDTO>> obterTodos(){
        return ResponseEntity.ok(tipoIngressoService.obterTodos());
    }

    @GetMapping("/obterporid/{id}")
    public ResponseEntity<TipoIngressoDTO> obetPorId(@PathVariable Long id){
        return ResponseEntity.ok(tipoIngressoService.obterPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<TipoIngressoDTO> salvar(@RequestBody @Valid TipoIngressoModel novoTipoIngresso){
        return ResponseEntity.ok(tipoIngressoService.salvar(novoTipoIngresso));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<TipoIngressoDTO> atualizar(@RequestBody @Valid TipoIngressoModel tipoIngressoExistente){
        return ResponseEntity.ok(tipoIngressoService.atualizar(tipoIngressoExistente));
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id){
        tipoIngressoService.deletar(id);
    }
}
