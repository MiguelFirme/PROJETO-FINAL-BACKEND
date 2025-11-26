package com.example.ProjetoFinal.Repositorys;


import com.example.ProjetoFinal.Entidades.Investimento;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {
    List<Investimento> findByCarteiraUsuarioId(UUID usuarioId);
    List<Investimento> findByCarteiraId(UUID carteiraId);

}