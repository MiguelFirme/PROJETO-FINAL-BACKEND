package com.example.ProjetoFinal.Services;

import com.example.ProjetoFinal.DTOs.InvestimentoRequest;
import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Entidades.Investimento;
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
        return investimentoRepository.findByCarteiraUsuarioId(usuarioId);
    }

    @Transactional
    public Investimento criarInvestimento(UUID usuarioId, InvestimentoRequest req) {
        Carteira carteira = carteiraRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carteira do usuário não encontrada"));

        String ticker = req.ticker.toUpperCase();
        double valor = req.valor;
        int dias = req.dias;

        // Buscar dados do ativo (map com campos da BRAPI)
        Map<String, Object> dados = ativoService.buscarAtivo(ticker);

        // Preço atual — tenta várias chaves comuns
        Double precoAtual = null;
        Object p = dados.get("regularMarketPrice");
        if (p == null) p = dados.get("close");
        if (p == null) p = dados.get("last");
        if (p != null) {
            try { precoAtual = Double.parseDouble(p.toString()); } catch (Exception ignored) { precoAtual = null; }
        }
        if (precoAtual == null) {
            throw new RuntimeException("Não foi possível obter preço atual para: " + ticker);
        }

        // Obter taxa anual (delegado para AtivoService)
        Double taxaAnual = ativoService.obterTaxaAnual(ticker, dados); // retorna valor em decimal (ex: 0.23 = 23% a.a.)
        if (taxaAnual == null) {
            // fallback conservador: 10% a.a.
            taxaAnual = 0.10;
        }

        // Converter taxa anual para taxa diária aproximada (assumindo 252 dias úteis)
        double taxaDiaria = Math.pow(1.0 + taxaAnual, 1.0 / 252.0) - 1.0;

        // Calcular projeção com juros compostos diários
        double valorFinal = valor * Math.pow(1.0 + taxaDiaria, dias);
        double rendimento = valorFinal - valor;

        Investimento inv = new Investimento();
        inv.setTicker(ticker);
        inv.setValorInvestido(valor);
        inv.setDias(dias);
        inv.setPrecoNoMomento(precoAtual);
        inv.setTaxaEstimativa(taxaDiaria);            // campo da entidade: taxaEstimativa
        inv.setValorEstimadoFinal(valorFinal);        // campo da entidade: valorEstimadoFinal
        inv.setRendimentoEstimado(rendimento);        // campo da entidade: rendimentoEstimado
        inv.setCarteira(carteira);

        return investimentoRepository.save(inv);
    }

    @Transactional
    public void deletar(Long id) {
        if (!investimentoRepository.existsById(id)) {
            throw new RuntimeException("Investimento não encontrado");
        }
        investimentoRepository.deleteById(id);
    }

    public List<Investimento> listarPorCarteira(UUID carteiraId) {
        return investimentoRepository.findByCarteiraId(carteiraId);
    }

    public Investimento buscarPorId(Long id) {
        return investimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));
    }

    @Transactional
    public Investimento atualizar(Long id, InvestimentoRequest req) {

        Investimento inv = investimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));

        inv.setTicker(req.ticker.toUpperCase());
        inv.setValorInvestido(req.valor);
        inv.setDias(req.dias);

        // Recalcular projeções
        Map<String, Object> dados = ativoService.buscarAtivo(inv.getTicker());

        Object p = dados.get("regularMarketPrice");
        if (p == null) p = dados.get("close");
        if (p == null) p = dados.get("last");

        Double precoAtual = (p != null) ? Double.parseDouble(p.toString()) : null;
        if (precoAtual == null) {
            throw new RuntimeException("Não foi possível obter preço atual para: " + inv.getTicker());
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
