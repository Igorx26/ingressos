package br.com.balada.ingressos.rest.controller;

import br.com.balada.ingressos.model.EventoModel;
import br.com.balada.ingressos.rest.dto.EventoDTO;
import br.com.balada.ingressos.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evento")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping("/obtertodos")
    public ResponseEntity<List<EventoDTO>> obterTodos(){
        //List<EventoDTO> eventoDTOList = eventoService.obterTodos();
        //return ResponseEntity.ok(eventoDTOList);
        return ResponseEntity.ok(eventoService.obterTodos());
    }

    @GetMapping("/obterporid/{id}")
    public ResponseEntity<EventoDTO> obetPorId(@PathVariable Long id){
        //List<EventoDTO> eventoDTOList = eventoService.obterTodos();
        //return ResponseEntity.ok(eventoDTOList);
        return ResponseEntity.ok(eventoService.obterPorId(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<EventoDTO> salvar(@RequestBody @Valid EventoModel novoEvento){
        //EventoDTO ecentoCadastradoDTO = eventoService.salvar(novoEvento);
        //return ResponseEntity.ok(ecentoCadastradoDTO);
        return ResponseEntity.ok(eventoService.salvar(novoEvento));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<EventoDTO> atualizar(@RequestBody @Valid EventoModel eventoExistente){
        return ResponseEntity.ok(eventoService.atualizar(eventoExistente));
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@PathVariable Long id){
        eventoService.deletar(id);
    }

}
