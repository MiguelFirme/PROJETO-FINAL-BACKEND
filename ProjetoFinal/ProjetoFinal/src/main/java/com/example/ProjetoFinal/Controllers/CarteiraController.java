package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Services.CarteiraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carteiras")
public class CarteiraController {

    private final CarteiraService carteiraService;

    public CarteiraController(CarteiraService carteiraService) {
        this.carteiraService = carteiraService;
    }

    // 🔎 Buscar carteira pelo ID do usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carteira> getByUsuario(@PathVariable UUID usuarioId) {
        Carteira carteira = carteiraService.buscarPorUsuarioId(usuarioId);
        return ResponseEntity.ok(carteira);
    }

    // 🔎 Buscar carteira pelo ID da carteira
    @GetMapping("/{carteiraId}")
    public ResponseEntity<Carteira> getById(@PathVariable UUID carteiraId) {
        Carteira carteira = carteiraService.buscarPorId(carteiraId);
        return ResponseEntity.ok(carteira);
    }
}
