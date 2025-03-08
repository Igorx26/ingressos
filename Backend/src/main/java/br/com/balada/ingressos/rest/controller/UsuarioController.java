package br.com.balada.ingressos.rest.controller;

import br.com.balada.ingressos.model.UsuarioModel;
import br.com.balada.ingressos.rest.dto.UsuarioDTO;
import br.com.balada.ingressos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/obtertodos")
    public ResponseEntity<List<UsuarioDTO>> obterTodos(){
        return ResponseEntity.ok(usuarioService.obterTodos());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDTO> salvar(@RequestBody @Valid UsuarioModel novoUsuario){
        return ResponseEntity.ok(usuarioService.salvar(novoUsuario));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@RequestBody @Valid UsuarioModel usuarioExistente){
        return ResponseEntity.ok(usuarioService.atualizar(usuarioExistente));
    }

    @DeleteMapping("/deletar/{id}")
    public void deletar(@RequestBody @Valid UsuarioModel usuarioExistente){
        usuarioService.deletar(usuarioExistente);
    }
}
