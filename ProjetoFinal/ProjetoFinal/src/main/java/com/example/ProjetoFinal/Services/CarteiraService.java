package com.example.ProjetoFinal.Services;

import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Repositorys.CarteiraRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarteiraService {

    private final CarteiraRepository carteiraRepository;

    public CarteiraService(CarteiraRepository carteiraRepository) {
        this.carteiraRepository = carteiraRepository;
    }

    public Carteira buscarPorId(UUID id) {
        return (Carteira) carteiraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada: " + id));
    }
}
