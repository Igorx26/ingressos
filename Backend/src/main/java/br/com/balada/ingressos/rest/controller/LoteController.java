package br.com.balada.ingressos.rest.controller;

import br.com.balada.ingressos.model.LoteModel;
import br.com.balada.ingressos.rest.dto.EventoDTO;
import br.com.balada.ingressos.rest.dto.LoteDTO;
import br.com.balada.ingressos.service.LoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lote")
public class LoteController {

    @Autowired
    private LoteService loteService;

    @GetMapping("/obtertodos")
    public ResponseEntity<List<LoteDTO>> obterTodos(){
        return ResponseEntity.ok(loteService.obterTodos());
    }

    @GetMapping("/obtertodosporevento/{id}")
    public ResponseEntity<List<LoteDTO>> obterTodosPorEvento(@PathVariable Long id){
        return ResponseEntity.ok(loteService.obterTodosPorEvento(id));
    }

    @GetMapping("/obterporid/{id}")
    public ResponseEntity<LoteDTO> obetPorId(@PathVariable Long id){
        return ResponseEntity.ok(loteService.obterPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<LoteDTO> salvar(@RequestBody @Valid LoteModel novoLote){
        return ResponseEntity.ok(loteService.salvar(novoLote));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<LoteDTO> atualizar(@RequestBody @Valid LoteModel loteExistente){
        return ResponseEntity.ok(loteService.atualizar(loteExistente));
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id){
        loteService.deletar(id);
    }
}
