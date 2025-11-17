package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.Entidades.Investimento;
import com.example.ProjetoFinal.Entidades.Usuario;
import com.example.ProjetoFinal.Services.InvestimentoService;
import com.example.ProjetoFinal.Services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<Investimento>> listarPorUsuario(@PathVariable UUID idUsuario) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        return ResponseEntity.ok(investimentoService.listarPorUsuario(usuario));
    }

    @PostMapping("/usuario/{idUsuario}")
    public ResponseEntity<Investimento> criar(@PathVariable UUID idUsuario, @RequestBody Investimento investimento) {
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
