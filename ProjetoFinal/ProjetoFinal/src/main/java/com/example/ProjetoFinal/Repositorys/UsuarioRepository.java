package com.example.ProjetoFinal.Repositorys;


import com.example.ProjetoFinal.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;


public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    void deleteById(UUID id);
}