package com.example.ProjetoFinal.Repositorys;


import com.example.ProjetoFinal.Entidades.Investimento;
import com.example.ProjetoFinal.Entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {
    List<Investimento> findByUsuario(Usuario usuario);
}
