package com.example.ProjetoFinal.Entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Carteira {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    UUID id;

    private String nome;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "carteira", cascade = CascadeType.ALL)
    private List<Ativo> ativos;

    public void adicionarAtivo(Ativo ativo) {
        ativo.setCarteira(this);
        ativos.add(ativo);
    }

    public UUID getId() {
        return id;
    };

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

    public List<Ativo> getAtivos() {
        return ativos;
    }

    public void setAtivos(List<Ativo> ativos) {
        this.ativos = ativos;
    }

}