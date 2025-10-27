package com.example.ProjetoFinal.Repositorys;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.ProjetoFinal.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    void deleteById(UUID id);

    boolean existsById(UUID id);

    Remapper findById(UUID id);
}
