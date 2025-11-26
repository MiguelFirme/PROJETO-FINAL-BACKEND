package com.example.ProjetoFinal.DTOs;

import java.time.LocalDateTime;


public class InvestimentoResponse {
    public Long id;
    public String ticker;
    public Double valorInvestido;
    public Integer dias;
    public Double precoNoMomento;
    public Double taxaEstimativa;
    public Double valorEstimadoFinal;
    public Double rendimentoEstimado;
    public LocalDateTime criadoEm;
}