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

@RestController
@RequestMapping("/api/carteiras")
public class CarteiraController {

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private AtivoRepository ativoRepository;

    @Autowired
    private AtivoService ativoService;

    // Adiciona um ativo (via código) à carteira do usuário
    @PostMapping("/{idCarteira}/adicionar-ativo/{codigo}")
    public ResponseEntity<?> adicionarAtivo(
            @PathVariable Long idCarteira,
            @PathVariable String codigo) {

        Carteira carteira = carteiraRepository.findById(idCarteira)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));

        // Busca informações reais da API externa
        Map<String, Object> dadosAtivo = ativoService.buscarAtivo(codigo);

        // Cria o ativo com base nas informações da API
        Ativo ativo = new Ativo();
        ativo.setCodigo(codigo);
        ativo.setPrecoAtual(Double.valueOf(dadosAtivo.get("regularMarketPrice").toString()));
        ativo.setCarteira(carteira);

        ativoRepository.save(ativo);

        return ResponseEntity.ok("Ativo " + codigo + " adicionado à carteira com sucesso!");
    }
}
