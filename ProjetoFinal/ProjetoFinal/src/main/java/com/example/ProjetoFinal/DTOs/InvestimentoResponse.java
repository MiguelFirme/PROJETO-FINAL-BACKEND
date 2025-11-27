package com.example.ProjetoFinal.DTOs;

import com.example.ProjetoFinal.Entidades.Investimento;
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

    public InvestimentoResponse(Investimento investimento) {
        this.id = investimento.getId();
        this.ticker = investimento.getTicker();
        this.valorInvestido = investimento.getValorInvestido();
        this.dias = investimento.getDias();
        this.precoNoMomento = investimento.getPrecoNoMomento();
        this.taxaEstimativa = investimento.getTaxaEstimativa();
        this.valorEstimadoFinal = investimento.getValorEstimadoFinal();
        this.rendimentoEstimado = investimento.getRendimentoEstimado();
        this.criadoEm = investimento.getCriadoEm();
    }
}
