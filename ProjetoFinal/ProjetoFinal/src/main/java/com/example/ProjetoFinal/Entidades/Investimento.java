package com.example.ProjetoFinal.Entidades;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Investimento {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String ticker;
    private Double valorInvestido;
    private Integer dias;


    private Double precoNoMomento;
    private Double taxaEstimativa;
    private Double valorEstimadoFinal;
    private Double rendimentoEstimado;


    private LocalDateTime criadoEm = LocalDateTime.now();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carteira_id")
    @JsonIgnore
    private Carteira carteira;


    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public Double getValorInvestido() { return valorInvestido; }
    public void setValorInvestido(Double valorInvestido) { this.valorInvestido = valorInvestido; }
    public Integer getDias() { return dias; }
    public void setDias(Integer dias) { this.dias = dias; }
    public Double getPrecoNoMomento() { return precoNoMomento; }
    public void setPrecoNoMomento(Double precoNoMomento) { this.precoNoMomento = precoNoMomento; }
    public Double getTaxaEstimativa() { return taxaEstimativa; }
    public void setTaxaEstimativa(Double taxaEstimativa) { this.taxaEstimativa = taxaEstimativa; }
    public Double getValorEstimadoFinal() { return valorEstimadoFinal; }
    public void setValorEstimadoFinal(Double valorEstimadoFinal) { this.valorEstimadoFinal = valorEstimadoFinal; }
    public Double getRendimentoEstimado() { return rendimentoEstimado; }
    public void setRendimentoEstimado(Double rendimentoEstimado) { this.rendimentoEstimado = rendimentoEstimado; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public Carteira getCarteira() { return carteira; }
    public void setCarteira(Carteira carteira) { this.carteira = carteira; }
}