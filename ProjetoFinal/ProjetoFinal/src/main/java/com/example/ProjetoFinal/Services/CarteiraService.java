package com.example.ProjetoFinal.Services;

import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Exceptions.ResourceNotFoundException;
import com.example.ProjetoFinal.Repositorys.CarteiraRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarteiraService {

    private final CarteiraRepository carteiraRepository;

    public CarteiraService(CarteiraRepository carteiraRepository) {
        this.carteiraRepository = carteiraRepository;
    }

    public Carteira buscarPorUsuarioId(UUID usuarioId) {
        return carteiraRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira", "usuário id", usuarioId));
    }

    public Carteira buscarPorId(UUID id) {
        return carteiraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira", "id", id));
    }
}
