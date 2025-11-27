package com.example.ProjetoFinal.DTOs;

import java.util.List;

public class UploadResponse {
    public int totalRegistros;
    public int sucesso;
    public int falha;
    public List<String> erros;

    public UploadResponse(int totalRegistros, int sucesso, int falha, List<String> erros) {
        this.totalRegistros = totalRegistros;
        this.sucesso = sucesso;
        this.falha = falha;
        this.erros = erros;
    }
}
