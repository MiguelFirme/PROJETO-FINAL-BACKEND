package com.example.ProjetoFinal.DTOs;

import com.example.ProjetoFinal.Entidades.Carteira;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CarteiraResponse {
    public UUID id;
    public List<InvestimentoResponse> investimentos;

    public CarteiraResponse(Carteira carteira) {
        this.id = carteira.getId();
        this.investimentos = carteira.getInvestimentos().stream()
                .map(InvestimentoResponse::new)
                .collect(Collectors.toList());
    }
}
