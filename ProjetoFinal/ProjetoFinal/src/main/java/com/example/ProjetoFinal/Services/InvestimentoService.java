package com.seuprojeto.service;

import com.seuprojeto.entity.Investimento;
import com.seuprojeto.entity.Usuario;
import com.seuprojeto.repository.InvestimentoRepository;
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
