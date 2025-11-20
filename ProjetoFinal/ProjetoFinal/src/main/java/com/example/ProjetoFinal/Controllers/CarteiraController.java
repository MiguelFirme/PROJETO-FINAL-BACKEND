package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.Entidades.Ativo;
import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Repositorys.AtivoRepository;
import com.example.ProjetoFinal.Repositorys.CarteiraRepository;
import com.example.ProjetoFinal.Services.AtivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carteiras")
public class CarteiraController {

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private AtivoRepository ativoRepository;

    @Autowired
    private AtivoService ativoService;

    // DTO para entrada
    public static class InvestimentoRequest {
        public String ativo;
        public Double valorInvestido;
    }

    @PostMapping("/{idCarteira}/investimentos")
    public ResponseEntity<?> adicionarInvestimento(
            @PathVariable UUID idCarteira,
            @RequestBody InvestimentoRequest body) {

        if (body.ativo == null || body.ativo.isEmpty()) {
            return ResponseEntity.badRequest().body("Campo 'ativo' é obrigatório.");
        }

        if (body.valorInvestido == null || body.valorInvestido <= 0) {
            return ResponseEntity.badRequest().body("Campo 'valorInvestido' deve ser maior que zero.");
        }

        // 1. Buscar carteira
        Carteira carteira = (Carteira) carteiraRepository.findById(idCarteira)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));

        // 2. Buscar preço do ativo via API externa (BRAPI)
        var dadosAtivo = ativoService.buscarAtivo(body.ativo);

        Object precoObj = dadosAtivo.get("regularMarketPrice");
        if (precoObj == null) {
            return ResponseEntity.status(500).body("Não foi possível obter o preço atual para o ativo: " + body.ativo);
        }

        double precoAtual;
        try {
            precoAtual = Double.parseDouble(precoObj.toString());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao converter preço do ativo: " + body.ativo);
        }

        // 3. Criar ativo e salvar
        Ativo ativo = new Ativo();
        ativo.setCodigo(body.ativo);
        ativo.setPrecoAtual(precoAtual);
        ativo.setValorInvestido(body.valorInvestido);
        ativo.setCarteira(carteira);

        ativoRepository.save(ativo);

        return ResponseEntity.ok(
                "Ativo " + body.ativo +
                        " adicionado! Valor investido: R$ " + body.valorInvestido +
                        ", preço atual: R$ " + precoAtual
        );
    }
}
