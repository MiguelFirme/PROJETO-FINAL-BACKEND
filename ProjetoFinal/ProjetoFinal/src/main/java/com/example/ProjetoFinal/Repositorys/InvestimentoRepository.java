package com.seuprojeto.repository;

import com.seuprojeto.entity.Investimento;
import com.seuprojeto.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {
    List<Investimento> findByUsuario(Usuario usuario);
}
