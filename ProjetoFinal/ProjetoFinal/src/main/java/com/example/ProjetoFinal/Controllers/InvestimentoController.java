package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.DTOs.InvestimentoRequest;
import com.example.ProjetoFinal.Entidades.Investimento;
import com.example.ProjetoFinal.Services.InvestimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    public InvestimentoController(InvestimentoService investimentoService) {
        this.investimentoService = investimentoService;
    }

    // 🔎 Listar investimentos de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Investimento>> listarPorUsuario(@PathVariable UUID usuarioId) {
        List<Investimento> lista = investimentoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/carteira/{carteiraId}")
    public ResponseEntity<List<Investimento>> listarPorCarteira(@PathVariable UUID carteiraId) {
        List<Investimento> investimentos = investimentoService.listarPorCarteira(carteiraId);
        return ResponseEntity.ok(investimentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investimento> buscarPorId(@PathVariable Long id) {
        Investimento investimento = investimentoService.buscarPorId(id);
        return ResponseEntity.ok(investimento);
    }


    // ➕ Criar investimento para um usuário
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Investimento> criar(
            @PathVariable UUID usuarioId,
            @RequestBody InvestimentoRequest req) {

        Investimento criado = investimentoService.criarInvestimento(usuarioId, req);
        return ResponseEntity.ok(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Investimento> atualizar(
            @PathVariable Long id,
            @RequestBody InvestimentoRequest req) {

        Investimento atualizado = investimentoService.atualizar(id, req);
        return ResponseEntity.ok(atualizado);
    }


    // 🗑️ Deletar investimento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        investimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
