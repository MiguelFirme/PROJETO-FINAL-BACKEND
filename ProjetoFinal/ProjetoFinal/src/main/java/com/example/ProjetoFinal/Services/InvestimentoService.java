package com.example.ProjetoFinal.Services;

import com.example.ProjetoFinal.Entidades.Investimento;
import com.example.ProjetoFinal.Entidades.Usuario;
import com.example.ProjetoFinal.Repositorys.InvestimentoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;

    public InvestimentoService(InvestimentoRepository investimentoRepository) {
        this.investimentoRepository = investimentoRepository;
    }

    public List<Investimento> listarTodos() {
        return investimentoRepository.findAll();
    }

    public List<Investimento> listarPorUsuario(Usuario usuario) {
        return investimentoRepository.findByUsuario(usuario);
    }

    public Investimento salvar(Investimento investimento) {
        return investimentoRepository.save(investimento);
    }

    public void deletar(Long id) {
        investimentoRepository.deleteById(id);
    }
}
