package com.example.ProjetoFinal.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    // --- RELAÇÃO COM USUÁRIO ---
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // --- RELAÇÃO COM INVESTIMENTOS ---
    @OneToMany(mappedBy = "carteira", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Investimento> investimentos = new ArrayList<>();

    // --- RELAÇÃO COM ATIVOS ---
    @OneToMany(mappedBy = "carteira", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ativo> ativos = new ArrayList<>();

    // --- MÉTODOS DE AJUDA ---
    public void adicionarAtivo(Ativo ativo) {
        ativo.setCarteira(this);
        ativos.add(ativo);
    }

    public void adicionarInvestimento(Investimento investimento) {
        investimento.setCarteira(this);
        investimentos.add(investimento);
    }

    // --- GETTERS E SETTERS ---
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Investimento> getInvestimentos() {
        return investimentos;
    }

    public void setInvestimentos(List<Investimento> investimentos) {
        this.investimentos = investimentos;
    }

    public List<Ativo> getAtivos() {
        return ativos;
    }

    public void setAtivos(List<Ativo> ativos) {
        this.ativos = ativos;
    }
}
