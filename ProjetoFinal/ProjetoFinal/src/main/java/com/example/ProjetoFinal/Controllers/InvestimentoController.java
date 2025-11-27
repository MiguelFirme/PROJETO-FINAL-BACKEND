package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.DTOs.InvestimentoRequest;
import com.example.ProjetoFinal.DTOs.InvestimentoResponse;
import com.example.ProjetoFinal.Entidades.Investimento;
import com.example.ProjetoFinal.DTOs.UploadResponse;
import com.example.ProjetoFinal.Services.InvestimentoService;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    public InvestimentoController(InvestimentoService investimentoService) {
        this.investimentoService = investimentoService;
    }

    //Listar investimentos de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<InvestimentoResponse>> listarPorUsuario(@PathVariable UUID usuarioId) {
        List<InvestimentoResponse> lista = investimentoService.listarPorUsuario(usuarioId).stream()
                .map(InvestimentoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/carteira/{carteiraId}")
    public ResponseEntity<List<InvestimentoResponse>> listarPorCarteira(@PathVariable UUID carteiraId) {
        List<InvestimentoResponse> investimentos = investimentoService.listarPorCarteira(carteiraId).stream()
                .map(InvestimentoResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(investimentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestimentoResponse> buscarPorId(@PathVariable Long id) {
        Investimento investimento = investimentoService.buscarPorId(id);
        return ResponseEntity.ok(new InvestimentoResponse(investimento));
    }


    //Criar investimento para um usuário
    @PostMapping("/usuario/{usuarioId}/upload")
    public ResponseEntity<UploadResponse> upload(
            @PathVariable UUID usuarioId,
            @RequestParam("file") MultipartFile file) {

        UploadResponse response = investimentoService.processarUploadInvestimentos(usuarioId, file);
        return ResponseEntity.ok(response);
    }

    //Criar investimento para um usuário
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<InvestimentoResponse> criar(
            @PathVariable UUID usuarioId,
            @Valid @RequestBody InvestimentoRequest req) {

        Investimento criado = investimentoService.criarInvestimento(usuarioId, req);
        return ResponseEntity.ok(new InvestimentoResponse(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvestimentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody InvestimentoRequest req) {

        Investimento atualizado = investimentoService.atualizar(id, req);
        return ResponseEntity.ok(new InvestimentoResponse(atualizado));
    }


    //Deletar investimento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        investimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
