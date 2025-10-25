package com.example.ProjetoFinal.Entidades;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

import com.example.ProjetoFinal.Entidades.Usuario;
import com.example.ProjetoFinal.Entidades.Ativo;

@Entity
public class Carteira {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    UUID id;

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

    public List<Ativo> getAtivos() {
        return ativos;
    }

    public void setAtivos(List<Ativo> ativos) {
        this.ativos = ativos;
    }

    private String nome;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "carteira", cascade = CascadeType.ALL)
    private List<Ativo> ativos;


}
