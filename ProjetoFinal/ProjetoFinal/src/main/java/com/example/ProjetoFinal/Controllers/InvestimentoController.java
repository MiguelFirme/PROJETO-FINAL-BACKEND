package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Entidades.Investimento;
import com.example.ProjetoFinal.Services.CarteiraService;
import com.example.ProjetoFinal.Services.InvestimentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;
    private final CarteiraService carteiraService;

    public InvestimentoController(InvestimentoService investimentoService, CarteiraService carteiraService) {
        this.investimentoService = investimentoService;
        this.carteiraService = carteiraService;
    }

    @GetMapping("/carteira/{idCarteira}")
    public ResponseEntity<List<Investimento>> listarPorCarteira(@PathVariable UUID idCarteira) {
        Carteira carteira = carteiraService.buscarPorId(idCarteira);
        return ResponseEntity.ok(investimentoService.listarPorCarteira(carteira));
    }

    @PostMapping("/carteira/{idCarteira}")
    public ResponseEntity<Investimento> criar(
            @PathVariable UUID idCarteira,
            @RequestBody Investimento investimento) {

        Carteira carteira = carteiraService.buscarPorId(idCarteira);

        investimento.setCarteira(carteira);

        Investimento criado = investimentoService.salvar(investimento);

        return ResponseEntity.ok(criado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        investimentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
