package com.example.ProjetoFinal.Entidades;

import com.example.ProjetoFinal.Entidades.Carteira;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Usuario implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    UUID id;

    private String nome;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Carteira carteira;


    //Getters e Setters gerados automaticamente
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

    public Carteira getCarteira() {
        return carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }
}
