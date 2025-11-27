package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.Services.AtivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ativos")
public class AtivoContoller {

    @Autowired
    private AtivoService ativoService;

    @GetMapping("/{codigo}")
    public ResponseEntity<Map<String, Object>> buscar(@PathVariable String codigo) {
        Map<String, Object> dados = ativoService.buscarAtivo(codigo);
        return ResponseEntity.ok(dados);
    }
}
