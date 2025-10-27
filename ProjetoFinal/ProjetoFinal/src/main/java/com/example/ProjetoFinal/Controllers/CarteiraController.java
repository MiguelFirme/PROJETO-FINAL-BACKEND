package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.Entidades.Ativo;
import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Repositorys.AtivoRepository;
import com.example.ProjetoFinal.Repositorys.CarteiraRepository;
import com.example.ProjetoFinal.Services.AtivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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

    @PostMapping("/{idCarteira}/adicionar-ativo")
    public ResponseEntity<?> adicionarAtivo(
            @PathVariable UUID idCarteira,
            @RequestBody Map<String, String> requestBody) {

        String codigo = requestBody.get("codigo");
        if (codigo == null || codigo.isEmpty()) {
            return ResponseEntity.badRequest().body("O campo 'codigo' é obrigatório.");
        }

        Carteira carteira = (Carteira) carteiraRepository.findById(idCarteira)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));

        Map<String, Object> dadosAtivo = ativoService.buscarAtivo(codigo);

        Object precoObj = dadosAtivo.get("regularMarketPrice");
        if (precoObj == null) {
            return ResponseEntity.status(500).body("Não foi possível obter o preço atual para o ativo: " + codigo);
        }

        Double precoAtual;
        try {
            if (precoObj instanceof Double) {
                precoAtual = (Double) precoObj;
            } else {
                precoAtual = Double.valueOf(precoObj.toString());
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(500).body("Erro de formato de preço para o ativo: " + codigo);
        }

        Ativo ativo = new Ativo();
        ativo.setCodigo(codigo);
        ativo.setPrecoAtual(precoAtual);
        ativo.setCarteira(carteira);

        ativoRepository.save(ativo);

        return ResponseEntity.ok("Ativo " + codigo + " adicionado à carteira com sucesso! Preço: " + String.format("%.2f", precoAtual));
    }
}