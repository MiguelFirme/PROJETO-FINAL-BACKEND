package com.example.ProjetoFinal.DTOs;

import com.example.ProjetoFinal.Entidades.Ativo;
import java.util.UUID;

public class AtivoResponse {
    public UUID id;
    public String nome;
    public Double valor;

    public AtivoResponse(Ativo ativo) {
        this.id = ativo.getId();
        this.nome = ativo.getNome();
        this.valor = ativo.getValor();
    }
}
