package com.example.ProjetoFinal.Repositorys;

import com.example.ProjetoFinal.Entidades.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
    Optional<Object> findById(UUID idCarteira);
}
