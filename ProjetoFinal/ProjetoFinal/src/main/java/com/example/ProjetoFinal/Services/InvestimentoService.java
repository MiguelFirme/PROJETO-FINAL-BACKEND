package com.example.ProjetoFinal.Services;

import com.example.ProjetoFinal.DTOs.InvestimentoRequest;
import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Entidades.Investimento;
import com.example.ProjetoFinal.Exceptions.ResourceNotFoundException;
import com.example.ProjetoFinal.Exceptions.ValidationException;
import com.example.ProjetoFinal.Repositorys.CarteiraRepository;
import com.example.ProjetoFinal.Repositorys.InvestimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final CarteiraRepository carteiraRepository;
    private final AtivoService ativoService;

    public InvestimentoService(InvestimentoRepository investimentoRepository,
                               CarteiraRepository carteiraRepository,
                               AtivoService ativoService) {
        this.investimentoRepository = investimentoRepository;
        this.carteiraRepository = carteiraRepository;
        this.ativoService = ativoService;
    }

    public List<Investimento> listarPorUsuario(UUID usuarioId) {
        // Não precisa de exceção aqui, pois uma lista vazia é um retorno válido.
        return investimentoRepository.findByCarteiraUsuarioId(usuarioId);
    }

    @Transactional
    public Investimento criarInvestimento(UUID usuarioId, InvestimentoRequest req) {
        Carteira carteira = carteiraRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Carteira", "usuário id", usuarioId));

        String ticker = req.ticker.toUpperCase();
        double valor = req.valor;
        int dias = req.dias;

        Map<String, Object> dados = ativoService.buscarAtivo(ticker);

        Double precoAtual = null;
        Object p = dados.get("regularMarketPrice");
        if (p == null) p = dados.get("close");
        if (p == null) p = dados.get("last");
        if (p != null) {
            try { precoAtual = Double.parseDouble(p.toString()); } catch (Exception ignored) { precoAtual = null; }
        }
        if (precoAtual == null) {
            throw new ValidationException("Não foi possível obter preço atual para o ativo: " + ticker);
        }

        // ❗ REGRA DE NEGÓCIO
        if (valor < precoAtual) {
            throw new ValidationException(
                    String.format("O valor investido (%.2f) não pode ser menor que o preço atual do ativo (%.2f).", valor, precoAtual)
            );
        }

        Double taxaAnual = ativoService.obterTaxaAnual(ticker, dados);
        if (taxaAnual == null) taxaAnual = 0.10;

        double taxaDiaria = Math.pow(1.0 + taxaAnual, 1.0 / 252.0) - 1.0;
        double valorFinal = valor * Math.pow(1.0 + taxaDiaria, dias);
        double rendimento = valorFinal - valor;

        Investimento inv = new Investimento();
        inv.setTicker(ticker);
        inv.setValorInvestido(valor);
        inv.setDias(dias);
        inv.setPrecoNoMomento(precoAtual);
        inv.setTaxaEstimativa(taxaDiaria);
        inv.setValorEstimadoFinal(valorFinal);
        inv.setRendimentoEstimado(rendimento);
        inv.setCarteira(carteira);

        return investimentoRepository.save(inv);
    }

    @Transactional
    public void deletar(Long id) {
        if (!investimentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Investimento", "id", id);
        }
        investimentoRepository.deleteById(id);
    }

    public List<Investimento> listarPorCarteira(UUID carteiraId) {
        // Não precisa de exceção aqui, pois uma lista vazia é um retorno válido.
        return investimentoRepository.findByCarteiraId(carteiraId);
    }

    public Investimento buscarPorId(Long id) {
        return investimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investimento", "id", id));
    }

    @Transactional
    public Investimento atualizar(Long id, InvestimentoRequest req) {

        Investimento inv = investimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Investimento", "id", id));

        inv.setTicker(req.ticker.toUpperCase());
        inv.setValorInvestido(req.valor);
        inv.setDias(req.dias);

        Map<String, Object> dados = ativoService.buscarAtivo(inv.getTicker());

        Double precoAtual = null;
        Object p = dados.get("regularMarketPrice");
        if (p == null) p = dados.get("close");
        if (p == null) p = dados.get("last");
        if (p != null) {
            try { precoAtual = Double.parseDouble(p.toString()); } catch (Exception ignored) { precoAtual = null; }
        }
        if (precoAtual == null) {
            throw new ValidationException("Não foi possível obter preço atual para o ativo: " + inv.getTicker());
        }

        // ❗ REGRA DE NEGÓCIO
        if (req.valor < precoAtual) {
            throw new ValidationException(
                    String.format("O valor investido (%.2f) não pode ser menor que o preço atual do ativo (%.2f).", req.valor, precoAtual)
            );
        }

        Double taxaAnual = ativoService.obterTaxaAnual(inv.getTicker(), dados);
        if (taxaAnual == null) taxaAnual = 0.10;

        double taxaDiaria = Math.pow(1 + taxaAnual, 1.0 / 252.0) - 1.0;
        double valorFinal = req.valor * Math.pow(1 + taxaDiaria, req.dias);
        double rendimento = valorFinal - req.valor;

        inv.setPrecoNoMomento(precoAtual);
        inv.setTaxaEstimativa(taxaDiaria);
        inv.setValorEstimadoFinal(valorFinal);
        inv.setRendimentoEstimado(rendimento);

        return investimentoRepository.save(inv);
    }
}
