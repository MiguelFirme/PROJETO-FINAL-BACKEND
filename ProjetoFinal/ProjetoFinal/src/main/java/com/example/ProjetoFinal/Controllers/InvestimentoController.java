package com.seuprojeto.controller;

import com.seuprojeto.entity.Investimento;
import com.seuprojeto.entity.Usuario;
import com.seuprojeto.service.InvestimentoService;
import com.seuprojeto.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;
    private final UsuarioService usuarioService;

    public InvestimentoController(InvestimentoService investimentoService, UsuarioService usuarioService) {
        this.investimentoService = investimentoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Investimento>> listarTodos() {
        return ResponseEntity.ok(investimentoService.listarTodos());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Investimento>> listarPorUsuario(@PathVariable Long idUsuario) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        return ResponseEntity.ok(investimentoService.listarPorUsuario(usuario));
    }

    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<Investimento> criar(@PathVariable Long idUsuario, @RequestBody Investimento investimento) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        investimento.setUsuario(usuario);
        Investimento criado = investimentoService.salvar(investimento);
        return ResponseEntity.ok(criado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        investimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
