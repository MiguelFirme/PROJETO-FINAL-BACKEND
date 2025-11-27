package com.example.ProjetoFinal.DTOs;

import com.example.ProjetoFinal.Entidades.Usuario;
import java.util.UUID;

public class UsuarioResponse {
    public UUID id;
    public String nome;
    public String email;
    public CarteiraResponse carteira;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        if (usuario.getCarteira() != null) {
            this.carteira = new CarteiraResponse(usuario.getCarteira());
        }
    }
}
