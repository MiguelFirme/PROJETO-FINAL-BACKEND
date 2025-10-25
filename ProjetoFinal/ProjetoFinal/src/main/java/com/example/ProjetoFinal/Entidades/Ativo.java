package com.example.ProjetoFinal.Entidades;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Ativo {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    UUID id;

    private String nome;
    private Double valor;

    public Carteira getCarteira() {
        return carteira;
    }

    @ManyToOne
    @JoinColumn(name = "carteira_id")
    private Carteira carteira;


    //Getters e Setters gerados automaticamente

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public void setCodigo(String codigo) {
    }
}
